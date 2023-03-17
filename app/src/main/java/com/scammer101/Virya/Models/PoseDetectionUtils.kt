package com.scammer101.Virya.Models

import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark
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

    fun accuracy_Treepose(angle_list: ArrayList<Double>, direction: String = "left"): Double {
        // lea, rea, lsa, rsa, lka, rka, lha, rha
        var actual1 = arrayListOf(160, 160, 100, 100, 180, 30, 180, 120)
        var actual2 = arrayListOf(160, 160, 100, 100, 30, 180, 120, 180)

        // for right
        if (direction == "right") {
            for (i in angle_list.indices) {
                // to do angle_list = 100--->80; actual = 90
                if (angle_list[i] > actual1[i]) {
                    var diff = angle_list[i] - actual1[i]    //diff = 100-90 = 10
                    angle_list[i] = actual1[i] - diff    //angle_list = 90 - 10 = 80
                }
            }

            return angle_list.sum() * 100 / actual1.sum()
        }

        // for left
        else if (direction == "left") {
            for (i in angle_list.indices) {
                // to do angle_list = 100--->80; actual = 90
                if (angle_list[i] > actual2[i]) {
                    var diff = angle_list[i] - actual2[i]    //diff = 100-90 = 10
                    angle_list[i] = actual2[i] - diff    //angle_list = 90 - 10 = 80
                }
            }

            return angle_list.sum().toDouble() * 100 / actual2.sum()
        }

        return 0.0
    }

    fun accuracy_Tpose(angle_list: ArrayList<Int>): Double {
        // lea, rea, lsa, rsa, lka, rka, lha, rha
        var actual1 = arrayListOf(180, 180, 90, 90, 180, 180, 180, 180)

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



    fun accuracy_Warrior2pose(angle_list: ArrayList<Double>, direction: String = "left"): Double {
        // lea, rea, lsa, rsa, lka, rka, lha, rha
        val actual1 = arrayListOf(180, 180, 90, 90, 100, 180, 100, 120)     // left
        val actual2 = arrayListOf(180, 180, 90, 90, 180, 100, 120, 100)     // right

        // for right
        if (direction == "right") {
            for (i in angle_list.indices) {
                // to do angle_list = 100--->80; actual = 90
                if (angle_list[i] > actual1[i]) {
                    var diff = angle_list[i] - actual1[i].toDouble()    //diff = 100-90 = 10
                    angle_list[i] = actual1[i] - diff    //angle_list = 90 - 10 = 80
                }
            }

            return angle_list.sum().toDouble() * 100 / actual1.sum()
        }

        // for left
        else if (direction == "left") {
            for (i in angle_list.indices) {
                // to do angle_list = 100--->80; actual = 90
                if (angle_list[i] > actual2[i]) {
                    val diff = angle_list[i] - actual2[i]    //diff = 100-90 = 10
                    angle_list[i] = actual2[i] - diff    //angle_list = 90 - 10 = 80
                }
            }

            return angle_list.sum().toDouble() * 100 / actual2.sum()
        }

        return 0.0
    }






    fun calculateAngle(a: PoseLandmark, b:PoseLandmark, c:PoseLandmark): Double {
        val angle = Math.toDegrees(
            atan2((c.position.x - b.position.y).toDouble(),
            (c.position.x - b.position.x).toDouble()
        ) - atan2((a.position.y - b.position.y).toDouble(),
            (a.position.x - b.position.x).toDouble()
        )
        )
        return if (angle > 180.0) 360 - angle else angle
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
            pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)!!)

        // Get the angle between the right hip,left hip and right knee points
        val rha = calculateAngle(pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)!!,
                pose.getPoseLandmark(PoseLandmark.LEFT_HIP)!!,
                pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)!!)

        return arrayListOf(lea, rea, lsa, rsa, lka, rka, lha, rha)

    }

}