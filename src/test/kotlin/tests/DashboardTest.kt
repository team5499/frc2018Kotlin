package tests

import kotlin.reflect.full.memberProperties
import kotlin.reflect.KMutableProperty

import org.junit.jupiter.api.Test

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
}
