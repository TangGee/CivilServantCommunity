package com.mdove.apt_test.annotation

import com.mdove.apt_annotation.TestAnnotation
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Messager
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.VariableElement
import javax.lang.model.util.Types
import javax.tools.Diagnostic

/**
 * Created by MDove on 2019-11-30.
 */
class TestProcessor :AbstractProcessor(){
    private var mMessager: Messager? = null
    private var mElementUtils: Elements? = null
    private var mTypesUtils: Types? = null
    private val mProxyMap = mutableMapOf<String,TestProcessor>()

    @Synchronized
    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        mMessager = processingEnv.messager
        mMessager?.printMessage(Diagnostic.Kind.ERROR,"日志开始---------------");
        mElementUtils = processingEnv.elementUtils
        mTypesUtils = processingEnv.typeUtils
    }

    override fun process(set: MutableSet<out TypeElement>, roundEnvironment: RoundEnvironment): Boolean {
        mMessager?.printMessage(Diagnostic.Kind.ERROR,"日志开始---------------");

        //得到所有的注解
        val elements = roundEnvironment.getElementsAnnotatedWith(TestAnnotation::class.java)
        for (element in elements) {
            val variableElement = element as VariableElement
            val classElement = variableElement.enclosingElement as TypeElement
            val fullClassName = classElement.qualifiedName.toString()
            var proxy = mProxyMap[fullClassName]
//            if (proxy == null) {
//                proxy = ClassCreatorProxy(mElementUtils, classElement)
//                mProxyMap[fullClassName] = proxy
//            }
//            val bindAnnotation = variableElement.getAnnotation(BindView::class.java)
//            val id = bindAnnotation.value()
//            proxy!!.putElement(id, variableElement)
        }
        //通过遍历mProxyMap，创建java文件
//        for (key in mProxyMap.keySet()) {
//            val proxyInfo = mProxyMap[key]
//            try {
//                mMessager.printMessage(
//                    Diagnostic.Kind.NOTE,
//                    " --> create " + proxyInfo.getProxyClassFullName()
//                )
//                val jfo = processingEnv.filer.createSourceFile(
//                    proxyInfo.getProxyClassFullName(),
//                    proxyInfo.getTypeElement()
//                )
//                val writer = jfo.openWriter()
//                writer.write(proxyInfo.generateJavaCode())
//                writer.flush()
//                writer.close()
//            } catch (e: IOException) {
//                mMessager.printMessage(
//                    Diagnostic.Kind.NOTE,
//                    " --> create " + proxyInfo.getProxyClassFullName() + "error"
//                )
//            }
//        }
        return true
    }
}