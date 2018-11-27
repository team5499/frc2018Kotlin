package frc.team5499.frc2018Kotlin.utils.math.splines

import frc.team5499.frc2018Kotlin.utils.math.geometry.Vector2
import frc.team5499.frc2018Kotlin.utils.math.geometry.Pose2d
import frc.team5499.frc2018Kotlin.utils.math.geometry.Pose2dWithCurvature

@SuppressWarnings("LongParameterList")
object SplineGenerator {

    private const val kMaxDX = 2.0
    private const val kMaxDY = 0.05
    private const val kMaxDTheta = 0.1
    private const val kMinSampleSize = 1

    fun parameterizeSpline(
        s: Spline,
        maxDx: Double,
        maxDy: Double,
        maxDTheta: Double,
        t0: Double,
        t1: Double
    ): MutableList<Pose2dWithCurvature> {
        val rv: MutableList<Pose2dWithCurvature> = mutableListOf()
        rv.add(s.getPoseWithCurvature(0.0))
        val dt = t1 - t0
        var t = 0.0
        while (t < t1) {
            getSegmentArc(s, rv, t0, t1, maxDx, maxDy, maxDTheta)
            t += dt / kMinSampleSize
        }
        return rv
    }

    fun parameterizeSpline(s: Spline): MutableList<Pose2dWithCurvature> {
        return parameterizeSpline(s, kMaxDX, kMaxDY, kMaxDTheta, 0.0, 1.0)
    }

    fun parameterizeSpline(
        s: Spline,
        maxDx: Double,
        maxDy: Double,
        maxDTheta: Double
    ): MutableList<Pose2dWithCurvature> {
        return parameterizeSpline(s, maxDx, maxDy, maxDTheta, 0.0, 1.0)
    }

    fun parameterizeSplines(
        splines: List<Spline>,
        maxDx: Double,
        maxDy: Double,
        maxDTheta: Double
    ): MutableList<Pose2dWithCurvature> {
        val rv: MutableList<Pose2dWithCurvature> = mutableListOf()
        if (splines.isEmpty()) return rv
        rv.add(splines.get(0).getPoseWithCurvature(0.0))
        for (s in splines) {
            val samples: MutableList<Pose2dWithCurvature> = parameterizeSpline(s, maxDx, maxDy, maxDTheta)
            samples.removeAt(0)
            rv.addAll(samples)
        }
        return rv
    }

    fun parameterizeSplines(splines: List<Spline>): MutableList<Pose2dWithCurvature> {
        return parameterizeSplines(splines, kMaxDX, kMaxDY, kMaxDTheta)
    }

    fun parameterizeSplines(vararg s: Spline): MutableList<Pose2dWithCurvature> {
        return parameterizeSplines(s.toMutableList())
    }

    private fun getSegmentArc(
        s: Spline,
        rv: MutableList<Pose2dWithCurvature>,
        t0: Double,
        t1: Double,
        maxDx: Double,
        maxDy: Double,
        maxDTheta: Double
    ) {
        val p0 = s.getPoint(t0)
        val p1 = s.getPoint(t1)
        val r0 = s.getHeading(t0)
        val r1 = s.getHeading(t1)
        val transformation = Pose2d(Vector2(p0, p1).rotateBy(r0.inverse()), r1.rotateBy(r0.inverse()))
        val twist = Pose2d.log(transformation)
        if (twist.dy > maxDy || twist.dx > maxDx || twist.dTheta > maxDTheta) {
            getSegmentArc(s, rv, t0, (t0 + t1) / 2, maxDx, maxDy, maxDTheta)
            getSegmentArc(s, rv, (t0 + t1) / 2, t1, maxDx, maxDy, maxDTheta)
        } else {
            // println(twist)
            rv.add(s.getPoseWithCurvature(t1))
        }
    }
}
