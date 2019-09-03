package com.mdove.herohalberd

object ClaymoreServiceLoader {

    private val staticCache: MutableMap<Class<*>, MutableList<*>> = mutableMapOf()
    private val dynamicCache: MutableMap<Class<*>, MutableList<*>> = mutableMapOf()

    private val loaders: MutableMap<Class<*>, LinkedHashSet<Loader<*>>> = mutableMapOf()

    abstract class Loader<T> {
        abstract fun newInstance(): T
        abstract fun identity(): Any
        override fun hashCode(): Int {
            return identity().hashCode()
        }

        override fun equals(other: Any?): Boolean {
            return identity() == (other as? Loader<T>)?.identity()
        }
    }

    @JvmStatic
    fun <T : Any> registerLoader(c: Class<T>, loader: Loader<T>) {
        if (loaders[c] == null) {
            loaders[c] = LinkedHashSet()
        }
        loaders[c]?.add(loader)
    }

    @JvmStatic
    fun <T : Any> loadAll(c: Class<T>): Iterator<T> {
        return object : Iterator<T> {

            private var index = 0

            override fun hasNext(): Boolean {
                while(index < getStaticCount(c)) {
                    try {
                        return getCacheOrCreate(c, index) != null
                    } catch (e: NoClassDefFoundError) {
                        /**
                         * getCacheOrCreate(c, index) might be from dynamic feature and not loaded.
                         * we just leave a null to take the place and keep trying until we enter dynamic zoom.
                         */
                        (staticCache.getOrPut(c) { mutableListOf<T?>() } as MutableList<T?>).add(null)
                        index++
                    }
                }
                return getCacheOrCreate(c, index) != null
            }

            override fun next(): T {
                return getCacheOrCreate(c, index++)!!
            }
        }
    }

    @JvmStatic
    @Throws(Exception::class)
    fun <T : Any> loadFirst(c: Class<T>): T {
        return loadAll(c).asSequence().first()
    }

    @JvmStatic
    fun <T : Any> loadFirstOrNull(c: Class<T>): T? {
        return try { loadAll(c).asSequence().first() } catch (e: Exception) { null }
    }

    private fun <T : Any> getCacheOrCreate(c: Class<T>, index: Int): T? {
        synchronized(this) {
            return getCacheOrNull(c, index) ?: createInstanceAndCache(c, index)
        }
    }

    /**
     * Will only be called when cache missed.
     */
    private fun <T : Any> createInstanceAndCache(c: Class<T>, index: Int): T? {
        return when {
            index < getStaticCount(c) -> createInstanceFromStatic(c, index)?.also { instance ->
                (staticCache.getOrPut(c) { mutableListOf<T?>() } as MutableList<T?>).also {
                    if (index < it.size) {
                        // Remove the potential null introduced by not yet loaded dynamic feature
                        it.removeAt(index)
                    }
                }.add(index, instance)
            }
            getDynamicCacheIndex(c, index) < loaders[c]?.size ?: 0 -> createInstanceFromLoader(c, getDynamicCacheIndex(c, index))?.also { instance ->
                (dynamicCache.getOrPut(c) { mutableListOf<T?>() } as MutableList<T?>).add(instance)
            }
            else -> null
        }
    }

    private fun <T : Any> createInstanceFromLoader(c: Class<T>, index: Int): T? {
        return loaders[c]?.toMutableList()?.getOrNull(index)?.newInstance() as T?
    }

    private fun <T : Any> getCacheOrNull(c: Class<T>, index: Int): T? {
        return getCacheOrNull(c, index, staticCache) ?: let {
            getCacheOrNull(c, getDynamicCacheIndex(c, index), dynamicCache)
        }
    }

    private inline fun <T : Any> getDynamicCacheIndex(c: Class<T>, index: Int): Int {
        return index - getStaticCount(c)
    }
    private fun <T : Any> getCacheOrNull(c: Class<T>, index: Int, cache: MutableMap<Class<*>, MutableList<*>>): T? {
        return (cache[c] as List<T>?)?.getOrNull(index)
    }

    /**
     * For class [c], there will be [0..[getStaticCount(c)]) different implementation. Otherwise it will return null
     */
    @JvmStatic
    fun <T : Any> createInstanceFromStatic(c: Class<T>, index: Int): T? {
        return null
    }

    @JvmStatic
    fun <T : Any> getStaticCount(c: Class<T>): Int {
        return 0
    }
}