package com.scammer101.Virya.Models

import android.util.Log
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark
import kotlin.math.abs
import kotlin.math.atan2

class PoseDetectionUtils {

    companion object {
        fun getPoseDetectionConfidence(pose: Pose): Float {
            var totalConfidence = 0f
            val landmarks = pose.allPoseLandmarks
            for (landmark in landmarks) {
                totalConfidence += landmark.inFrameLikelihood
            }
            return totalConfidence / landmarks.size
        }
    }

    fun accuracy_Treepose(angle_list: ArrayList<Double>): Double {
        // lea, rea, lsa, rsa, lka, rka, lha, rha
        var actual1 = arrayListOf(145, 145, 165, 165, 180, 30, 90, 120)
        var actual2 = arrayListOf(145, 145, 165, 165, 30, 180, 120, 90)
        var p1=00.00
        var p2=00.00
        var angle_list2 : ArrayList<Double> = arrayListOf()
        angle_list2.addAll(angle_list)

        // for right

            for (i in angle_list.indices) {
                // to do angle_list = 100--->80; actual = 90
                if (angle_list[i] > actual1[i]) {
                    var diff = angle_list[i] - actual1[i]    //diff = 100-90 = 10
                    angle_list[i] = actual1[i] - diff    //angle_list = 90 - 10 = 80
                }
            }

           p1 =  angle_list.sum() * 100 / actual1.sum()


        // for left
            for (i in angle_list2.indices) {
                // to do angle_list = 100--->80; actual = 90
                if (angle_list2[i] > actual2[i]) {
                    var diff = angle_list2[i] - actual2[i]    //diff = 100-90 = 10
                    angle_list2[i] = actual2[i] - diff    //angle_list = 90 - 10 = 80
                }
            }

            p2 =  angle_list2.sum().toDouble() * 100 / actual2.sum()

        Log.d("LargeP", "p1" + p1.toString())
        Log.d("LargeP", "p2" + p2.toString())
        return Math.max(p1, p2)
    }

    fun accuracy_Tpose(angle_list: ArrayList<Double>): Double {
        // lea, rea, lsa, rsa, lka, rka, lha, rha
        var actual1 = arrayListOf(180, 180, 90, 90, 180, 180, 90, 90)

        // Condition for percentage > 100
        for (i in angle_list.indices) {
            // to do angle_list = 100--->80; actual = 90
            if (angle_list[i] > actual1[i]) {
                var diff = angle_list[i] - actual1[i]    //diff = 100-90 = 10
                angle_list[i] = actual1[i] - diff    //angle_list = 90 - 10 = 80
            }
        }

        return angle_list.sum().toDouble() * 100 / actual1.sum()
    }

    fun accuracy_Warrior2pose(angle_list: ArrayList<Double>): Double {
        // lea, rea, lsa, rsa, lka, rka, lha, rha
        val actual1 = arrayListOf(180, 180, 90, 90, 100, 180, 120, 140)     // left
        val actual2 = arrayListOf(180, 180, 90, 90, 180, 100, 140, 120)     // right
        var angle_list2 : ArrayList<Double> = angle_list
        var p1=00.00
        var p2=00.00

        // for right
            for (i in angle_list.indices) {
                // to do angle_list = 100--->80; actual = 90
                if (angle_list[i] > actual1[i]) {
                    var diff = angle_list[i] - actual1[i].toDouble()    //diff = 100-90 = 10
                    angle_list[i] = actual1[i] - diff    //angle_list = 90 - 10 = 80
                }
            }

            p1 =  angle_list.sum().toDouble() * 100 / actual1.sum()


        // for left
            for (i in angle_list2.indices) {
                // to do angle_list = 100--->80; actual = 90
                if (angle_list2[i] > actual2[i]) {
                    val diff = angle_list2[i] - actual2[i]    //diff = 100-90 = 10
                    angle_list2[i] = actual2[i] - diff    //angle_list = 90 - 10 = 80
                }
            }

            p2 =  angle_list2.sum() * 100 / actual2.sum()


        return Math.max(p1, p2)
    }

    fun calculateAngle(a: PoseLandmark, b:PoseLandmark, c:PoseLandmark): Double {
        var angle = Math.toDegrees(
            atan2((c.position.y - b.position.y).toDouble(),
            (c.position.x - b.position.x).toDouble()
        ) - atan2((a.position.y - b.position.y).toDouble(),
            (a.position.x - b.position.x).toDouble()
        )
        )
        Log.v("angleCheck", angle.toString())
        angle = abs(angle)
        return (if (angle > 180.0) 360 - angle else angle)
    }

    fun pose_angles(pose:Pose): ArrayList<Double> {

        // Get the angle between the left shoulder, elbow and wrist points.
        val lea = calculateAngle(pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)!!,
            pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)!!,
            pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)!!)

        // Get the angle between the right shoulder, elbow and wrist points.
        val rea = calculateAngle(pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)!!,
            pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)!!,
            pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)!!)

        // Get the angle between the left elbow, shoulder and hip points.
        val lsa = calculateAngle(pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)!!,
            pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)!!,
            pose.getPoseLandmark(PoseLandmark.LEFT_HIP)!!)

        // Get the angle between the right hip, shoulder and elbow points.
        val rsa = calculateAngle(pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)!!,
            pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)!!,
            pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)!!)

        // Get the angle between the left hip, knee and ankle points.
        val lka = calculateAngle(pose.getPoseLandmark(PoseLandmark.LEFT_HIP)!!,
            pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)!!,
            pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)!!)

        // Get the angle between the right hip, knee and ankle points
        val rka = calculateAngle(pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)!!,
            pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)!!,
            pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE)!!)

        // Get the angle between the left hip, right hip and left knee points.
        val lha = calculateAngle(pose.getPoseLandmark(PoseLandmark.LEFT_HIP)!!,
            pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)!!,
            pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)!!)

        // Get the angle between the right hip,left hip and right knee points
        val rha = calculateAngle(pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)!!,
                pose.getPoseLandmark(PoseLandmark.LEFT_HIP)!!,
                pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)!!)

        return arrayListOf(lea, rea, lsa, rsa, lka, rka, lha, rha)

    }

}