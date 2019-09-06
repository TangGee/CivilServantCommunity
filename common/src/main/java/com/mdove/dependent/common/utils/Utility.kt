package com.mdove.dependent.common.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Parcel
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.graphics.applyCanvas
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import com.mdove.dependent.common.BuildConfig
import com.mdove.dependent.common.gson.GsonProvider
import com.mdove.dependent.common.network.NormalResp
import com.mdove.dependent.common.network.ServerRespException
import org.json.JSONArray
import org.json.JSONObject
import java.io.Closeable

/**
 * Created by MDove on 2019/9/6.
 */
fun Int.dpToPx(context: Context) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    context.resources.displayMetrics
)

fun Float.dpToPx(context: Context) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics)

/**
 * T can be Array but can not be Collection
 */
inline fun <reified T : Any> fromJson(json: String): T {
    return GsonProvider.getDefaultGson().fromJson(json, T::class.java)
}

inline fun <reified T : Any> fromJsonSafely(json: String?): T? {
    json ?: return null
    return try {
        fromJson(json)
    } catch (e: Exception) {
        null
    }
}

fun Any.toJson() = GsonProvider.getDefaultGson().toJson(this)

fun Any.toJsonObj() = JSONObject(this.toJson())

fun CharSequence.toJsonObj() = JSONObject(this.toString())

inline fun <reified T : Any> fromJson(json: JSONObject) =
    GsonProvider.getDefaultGson().fromJson(json.toString(), T::class.java)

fun Collection<JSONObject>.toJsonArray(): JSONArray {
    val ja = JSONArray()
    forEach { ja.put(it) }
    return ja
}

fun Array<JSONObject>.toJsonArray(): JSONArray {
    val ja = JSONArray()
    forEach { ja.put(it) }
    return ja
}

fun LongArray.toJsonArray(): JSONArray {
    val ja = JSONArray()
    forEach { ja.put(it) }
    return ja
}

/**
 * @param i Intent
 * @param requestCode request code
 * @param useFirstPackage use the first one if more than one handler
 * @param forceShowChooser show selector when more than one handler, even if there is a default handler
 * @param chooserTitle the title of the app selector
 */
@JvmOverloads
fun Activity.safeStartAct(
    i: Intent,
    requestCode: Int = -1,
    useFirstPackage: Boolean = false,
    forceShowChooser: Boolean = false,
    chooserTitle: String = ""
): Boolean {
    val pm = packageManager
    val r = pm.queryIntentActivities(i, PackageManager.MATCH_DEFAULT_ONLY)
    return if (r.size > 0) {
        if (useFirstPackage || r.size == 1) {
            i.`package` = r[0].activityInfo.packageName
        } else if (forceShowChooser) {
            val s = Intent.createChooser(i, chooserTitle)
            return tryStartActivityForResult(s, requestCode)
        }
        return tryStartActivityForResult(i, requestCode)
    } else {
        return false
    }
}

private fun Activity.tryStartActivityForResult(intent: Intent, requestCode: Int): Boolean {
    try {
        startActivityForResult(intent, requestCode)
    } catch (e: Exception) {
        return false
    }
    return true
}

private fun Fragment.tryStartActivityForResult(intent: Intent, requestCode: Int): Boolean {
    try {
        startActivityForResult(intent, requestCode)
    } catch (e: Exception) {
        return false
    }
    return true
}

/**
 * @param i Intent
 * @param requestCode request code
 * @param useFirstPackage use the first one if more than one handler
 * @param forceShowChooser show selector when more than one handler, even if there is a default handler
 * @param chooserTitle the title of the app selector
 */
@JvmOverloads
fun Fragment.safeStartAct(
    i: Intent,
    requestCode: Int = 0,
    useFirstPackage: Boolean = false,
    forceShowChooser: Boolean = false,
    chooserTitle: String = ""
): Boolean {
    val pm = context?.packageManager ?: return false
    val r = pm.queryIntentActivities(i, PackageManager.MATCH_DEFAULT_ONLY)
    return if (r.size > 0) {
        if (useFirstPackage || r.size == 1) {
            i.`package` = r[0].activityInfo.packageName
        } else if (forceShowChooser) {
            val s = Intent.createChooser(i, chooserTitle)
            tryStartActivityForResult(s, requestCode)
            return true
        }
        tryStartActivityForResult(i, requestCode)
        true
    } else {
        false
    }
}

@SuppressLint("ShowToast")
inline fun Toast.showInCenter() {
    setGravity(Gravity.CENTER, 0, 0)
    show()
}

fun Uri?.isImage(ctx: Context): Boolean {
    if (this == null) {
        Log.d("zs_image_uri", "$this is not image")
        return false
    }
    try {
        val input = ctx.contentResolver.openInputStream(this)
        input.use { input ->
            val ops = BitmapFactory.Options()
            ops.inJustDecodeBounds = true
            BitmapFactory.decodeStream(input, null, ops)
            val res = ops.outWidth > 0 && ops.outHeight > 0
            Log.d("zs_image_uri", "$this is image: $res")
            return res
        }
    } catch (e: Throwable) {
        Log.d("zs_image_uri", "$this is not image: $e", e)
        return false
    }
}

fun CharSequence?.safeToUri(): Uri? = if (this == null) {
    null
} else {
    try {
        Uri.parse(this.toString())
    } catch (e: Throwable) {
        null
    }
}


fun View.showKeyboard() {
    if (width > 0 && height > 0) {
        requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, 0)
    } else {
        viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                } else {
                    viewTreeObserver.removeGlobalOnLayoutListener(this)
                }
                requestFocus()
                val imm =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(this@showKeyboard, 0)
            }
        })
    }
}

fun View.toggleKeyboard() {
    if (width > 0 && height > 0) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS)
    } else {
        viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                } else {
                    viewTreeObserver.removeGlobalOnLayoutListener(this)
                }
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        })
    }
}

fun Activity.hideKeyboard() {
    if (currentFocus == null) {
        Handler().postDelayed({
            if (currentFocus == null) {
                return@postDelayed
            }
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }, 100)
        return
    }
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    postDelayed({
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && isAttachedToWindow)
            || (windowToken != null && hasWindowFocus())
        ) {
            try {
                imm.hideSoftInputFromWindow(windowToken, 0)
                clearFocus()
            } catch (e: Throwable) {
//                CrashlyticsUtils.safeLogException(e)
            }
        }
    }, 100)
}

fun Intent.grantUriPermission(ctx: Context, uri: Uri) {
    addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    val pm = ctx.packageManager
    val r = pm.queryIntentActivities(this, PackageManager.MATCH_DEFAULT_ONLY)
    r.forEach {
        val packageName = it.activityInfo.packageName
        ctx.grantUriPermission(
            packageName,
            uri,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
    }
}

val Context.windowWidth: Int
    get() = resources.displayMetrics.widthPixels
val Context.windowHeight: Int
    get() = resources.displayMetrics.heightPixels

inline fun Closeable?.closeSafely() {
    try {
        this?.close()
    } catch (e: Throwable) {
        // ignore exp
        e.printStackTrace()
    }
}

/**
 * 由于android.database.Cursor在API LEVEL 16之后才继承了Closeable接口，
 * 因此单独定义
 */
inline fun Cursor?.closeSafely() {
    try {
        this?.close()
    } catch (e: Throwable) {
        // ignore exp
        e.printStackTrace()
    }
}

/**
 * 检测Intent指向的某个Activity是否存在，保证安全，通常在启动某个界面之前进行检测
 */
inline fun Intent.activityAvailable(context: Context): Boolean {
    return try {
        val activities = context.packageManager
            .queryIntentActivities(this, PackageManager.MATCH_DEFAULT_ONLY)
        activities != null && activities.size > 0
    } catch (exp: Throwable) {
        false
    }
}

inline fun <reified T : Any> getBaseResp(json: String): NormalResp<T> {
    val normalResp: NormalResp<T> =
        GsonProvider.getDefaultGson().fromJson(json, object : TypeToken<NormalResp<T>>() {}.type)
//    if (baseResp.permissionStatus == NormalResp.STATUS_CODE_PERMISSION_DENIED) {
//        throw ForbiddenException(baseResp.permissionStatus)
//    }
    return normalResp
}

inline fun <reified T : Any> fromServerResp(json: String): NormalResp<T> {
    val normalResp: NormalResp<T> =
        GsonProvider.getDefaultGson().fromJson(json, object : TypeToken<NormalResp<T>>() {}.type)
//    if (baseResp.permissionStatus == NormalResp.STATUS_CODE_PERMISSION_DENIED) {
//        throw ForbiddenException(baseResp.permissionStatus)
//    }
    if (normalResp.isSuccess) {
//        val dataJson = JsonParser().parse(json).asJsonObject.getAsJsonObject("data") // FIX BUG OF LONG TYPE PARSING
        return normalResp
    } else {
        throw ServerRespException(errorCode = normalResp.errorCode, resp = JsonParser().parse(json))
    }
}


inline fun <reified T : Any> Fragment.getFromStack(): T? {
    if (this is T) {
        return this
    }
    var pf = parentFragment
    while (pf != null) {
        if (pf is T) {
            return pf
        }
        pf = pf.parentFragment
    }

    return activity as? T
}

fun Fragment.getStackInfo(): String {
    val builder = StringBuilder()
    builder.append("\n====== Stack of Fragment ${this::class.java.name} Start ======")
    builder.append("\n--> ${this::class.java.name}")
    var pf = parentFragment
    while (pf != null) {
        builder.append("\n--> ${pf::class.java.name}")
        pf = pf.parentFragment
    }

    val a = activity
    if (a != null) {
        builder.append("\n--> ${a::class.java.name}")
    } else {
        builder.append("\n--> NOT ATTACHED TO ACTIVITY")
    }
    builder.append("\n====== Stack of Fragment ${this::class.java.name} End ======")

    return builder.toString()
}

fun CharSequence.clipUrl() =
    """https?://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]""".toRegex().find(this)?.groups?.get(
        0
    )?.value
        ?: ""

fun CharSequence.isUrl() =
    """^https?://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]$""".toRegex().matches(this)

inline fun <T : SQLiteDatabase, R> T.use(block: (T) -> R): R {
    var closed = false
    try {
        return block(this)
    } catch (e: Exception) {
        closed = true
        try {
            close()
        } catch (closeException: Exception) {
            // eat the closeException as we are already throwing the original cause
            // and we don't want to mask the real exception
        }
        throw e
    } finally {
        if (!closed) {
            close()
        }
    }
}


inline fun <T : Cursor, R> T.use(block: (T) -> R): R {
    var closed = false
    try {
        return block(this)
    } catch (e: Exception) {
        closed = true
        try {
            close()
        } catch (closeException: Exception) {
            // eat the closeException as we are already throwing the original cause
            // and we don't want to mask the real exception
        }
        throw e
    } finally {
        if (!closed) {
            close()
        }
    }
}

fun createVideoThumbnail(filePath: String, kind: Int, timeUs: Long = -1L): Bitmap? {
    var bitmap: Bitmap? = null
    val retriever = MediaMetadataRetriever()
    try {
        retriever.setDataSource(filePath)
        bitmap = retriever.getFrameAtTime(timeUs)
    } catch (ex: IllegalArgumentException) {
        // Assume this is a corrupt video file
        Log.e("createVideoThumbnail", "", ex)
    } catch (ex: RuntimeException) {
        // Assume this is a corrupt video file.
        Log.e("createVideoThumbnail", "", ex)
    } finally {
        try {
            retriever.release()
        } catch (ex: RuntimeException) {
            // Ignore failures while cleaning up.
            Log.e("createVideoThumbnail", "", ex)
        }

    }

    if (bitmap == null) return null

    if (kind == MediaStore.Images.Thumbnails.MINI_KIND) {
        // Scale down the bitmap if it's too large.
        val width = bitmap.width
        val height = bitmap.height
        val max = Math.max(width, height)
        if (max > 512) {
            val scale = 512f / max
            val w = Math.round(scale * width)
            val h = Math.round(scale * height)
            bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true)
        }
    } else if (kind == MediaStore.Images.Thumbnails.MICRO_KIND) {
        bitmap = ThumbnailUtils.extractThumbnail(
            bitmap,
            96, // ThumbnailUtils.TARGET_SIZE_MICRO_THUMBNAIL,
            96, // ThumbnailUtils.TARGET_SIZE_MICRO_THUMBNAIL,
            ThumbnailUtils.OPTIONS_RECYCLE_INPUT
        )
    }
    return bitmap
}

fun Throwable.getStackTraceStr(): String {
    return stackTrace?.filterNotNull()?.joinToString("\n") { it.toString() } ?: ""
}

fun <T> List<T>?.getSafely(pos: Int?): T? {
    if (this == null || pos == null || pos < 0) {
        return null
    }
    if (size > pos) {
        return get(pos)
    }
    return null
}

fun <K, V> Map<K, V>?.getSafely(key: K?): V? {
    if (this.isNullOrEmpty() || key == null) {
        return null
    }
    return get(key = key)
}

fun View.drawToBitmap(config: Bitmap.Config = Bitmap.Config.ARGB_8888, clipToChild: View): Bitmap {
    if (!ViewCompat.isLaidOut(this)) {
        throw IllegalStateException("View needs to be laid out before calling drawToBitmap()")
    }

    var left = clipToChild.left
    var top = clipToChild.top
    var child = clipToChild
    while (child.parent != this) {
        child = child.parent as View
        left += child.left
        top += child.top
    }
    return Bitmap.createBitmap(clipToChild.width, clipToChild.height, config).applyCanvas {
        translate(-scrollX.toFloat(), -scrollY.toFloat())
        translate(-left.toFloat(), -top.toFloat())
        draw(this)
    }
}

fun JSONObject.forEach(action: (name: String, value: Any) -> Unit) {
    val names = names()

    names?.let {
        for (i in 0 until names.length()) {
            val name = names[i] as String
            action(name, get(name))
        }
    }

}

fun decodeImageSize(path: String): IntArray {
    val opt = BitmapFactory.Options()
    opt.inJustDecodeBounds
    BitmapFactory.decodeFile(path, opt)
    return intArrayOf(opt.outWidth, opt.outHeight)
}

fun decodeVideoDuration(filePath: String): Long {
    var parser: MediaMetadataRetriever? = null
    return try {
        parser = MediaMetadataRetriever()
        parser.setDataSource(filePath)
        parser.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: -1L
    } catch (e: Throwable) {
        if (BuildConfig.DEBUG) {
            throw RuntimeException("error decode duration of file: $filePath", e)
        }
        -1L
    } finally {
        try {
            parser?.release()
        } catch (ignore: Throwable) {
        }
    }
}

fun SQLiteDatabase.getColumnNames(tableName: String): List<String> {
    val result = mutableListOf<String>()
    val res = rawQuery("PRAGMA table_info($tableName)", null)
    res?.use {
        res.moveToFirst()
        do {
            result.add(res.getString(1))
        } while (res.moveToNext())
    }
    return result
}

fun <T> Collection<T>.toArrayList(): ArrayList<T> {
    val res = ArrayList<T>()
    res.addAll(this)
    return res
}

inline fun <reified T : Parcelable> Parcel.readTypedArrayList(): ArrayList<T>? {
    return readInt().let {
        if (it < 0) {
            null
        } else {
            ArrayList<T>(it).apply {
                for (i in 0 until it) {
                    readParcelable<T>(T::class.java.classLoader)?.let { add(it) }
                }
            }
        }
    }
}

/**
 * 低版本的API没有isDestroyed
 */
fun Activity.isDestroyedSafe(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
        isDestroyed
    } else {
        false
    }
}
