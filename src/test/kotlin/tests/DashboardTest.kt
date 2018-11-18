package tests

import kotlin.reflect.full.memberProperties
import kotlin.reflect.KMutableProperty

import org.junit.jupiter.api.Test

import org.reflections.Reflections
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import org.reflections.scanners.SubTypesScanner
import org.reflections.scanners.ResourcesScanner
import org.reflections.util.FilterBuilder

import frc.team5499.frc2018Kotlin.dashboard.Dashboard
import frc.team5499.frc2018Kotlin.dashboard.DashboardClient

public class DashboardTest {

    @Dashboard("REFLECT_TEST")
    var reflectTest: String = ""

    private fun bindReflectionValue(obj: Any) {
        val declaredProperties = obj::class.memberProperties

        for (prop in declaredProperties) {
            for (ann in prop.annotations) {
                println(ann)
                when (ann) {
                    is Dashboard -> {
                        (prop as KMutableProperty<*>).setter.call(obj, ann.name)
                    }
                }
            }
        }
    }

    @Test
    fun annotationTest() {
        bindReflectionValue(this)
        println(reflectTest)
        DashboardClient.testFun()
        assert(reflectTest == "REFLECT_TEST")
    }

    @Test
    fun packageTest() {
        var classLoadersList = mutableListOf<ClassLoader>()
        classLoadersList.add(ClasspathHelper.contextClassLoader())
        classLoadersList.add(ClasspathHelper.staticClassLoader())

        var reflections: Reflections = Reflections(ConfigurationBuilder()
            .setScanners(SubTypesScanner(false /* don't exclude Object.class */), ResourcesScanner())
            .setUrls(ClasspathHelper.forClassLoader(classLoadersList[0]))
            .filterInputsBy(FilterBuilder().include(FilterBuilder.prefix("tests"))))

        var classes: Set<Class<*>> = reflections.getSubTypesOf(Any::class.java)

        println(classes)
    }
}
