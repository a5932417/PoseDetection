package tw.edu.pu.s1071928.posedetection

import android.content.Intent
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import kotlinx.android.synthetic.main.activity_main.*
import org.tensorflow.lite.support.image.TensorImage
import tw.edu.pu.s1071928.posedetection.ml.Model
import tw.edu.pu.s1071928.posedetection.ml.Model1

class MainActivity : AppCompatActivity() {

    lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var imgUri = intent.getStringExtra("imguri")

        //將drawable的圖片轉換成Bitmap
        val bitmap: Bitmap = BitmapFactory.decodeResource(resources,R.drawable.no)

        val model = Model1.newInstance(this)

// Creates inputs for reference.
        val imaget = TensorImage.fromBitmap(bitmap)

// Runs model inference and gets result.
        val outputs = model.process(imaget)
        val probability = outputs.probabilityAsCategoryList

// Releases model resources if no longer used.
        model.close()

        val txv: TextView = findViewById(R.id.txv)
        txv.text = probability.toString()

        img.setImageBitmap(bitmap)

        /*
        // Base pose detector with streaming frames, when depending on the pose-detection sdk
        val options = PoseDetectorOptions.Builder()
            .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
            .build()
         */

        // Accurate pose detector on static images, when depending on the pose-detection-accurate sdk
        val options = AccuratePoseDetectorOptions.Builder()
            .setDetectorMode(AccuratePoseDetectorOptions.SINGLE_IMAGE_MODE)
            .build()
        val poseDetector = PoseDetection.getClient(options)

        //Prepare the input image
        val image = InputImage.fromBitmap(bitmap, 0)

        fun render(pose: Pose) {

            if (pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER) == null) {
                return
            }

            val dstBitmap = Bitmap.createBitmap(
                bitmap.width, bitmap.height,
                Bitmap.Config.ARGB_8888
            )
            var canvas = Canvas(dstBitmap)

            //繪製原始圖片
            canvas.drawBitmap(bitmap, 0f, 0f, null)

            //設定畫筆
            val paint = Paint()
            paint.color = Color.RED
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 10f

            //Left
            val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
            val leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)
            val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
            val leftPinky = pose.getPoseLandmark(PoseLandmark.LEFT_PINKY)
            val leftIndex = pose.getPoseLandmark(PoseLandmark.LEFT_INDEX)
            val leftThumb = pose.getPoseLandmark(PoseLandmark.LEFT_THUMB)

            //Right
            val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)
            val rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)
            val rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)
            val rightPinky = pose.getPoseLandmark(PoseLandmark.RIGHT_PINKY)
            val rightIndex = pose.getPoseLandmark(PoseLandmark.RIGHT_INDEX)
            val rightThumb = pose.getPoseLandmark(PoseLandmark.RIGHT_THUMB)

            /*val left = arrayOf(leftShoulder, leftElbow, leftWrist, leftPinky, leftIndex, leftThumb)
            val right = arrayOf(rightShoulder, rightElbow, rightWrist, rightPinky, rightIndex, rightThumb)*/

            var xShouder = leftShoulder.position.x
            var yShouder = leftShoulder.position.y
            var xElbow = leftElbow.position.x
            var yElbow = leftElbow.position.y
            var xWrist = leftWrist.position.x
            var yWrist = leftWrist.position.y
            var xIndex = leftIndex.position.x
            var yIndex = leftIndex.position.y
            var xThumb = leftThumb.position.x
            var yThumb = leftThumb.position.y
            var xPinky = leftPinky.position.x
            var yPinky = leftPinky.position.y

            canvas.drawCircle(xShouder, yShouder, 10f, paint)
            canvas.drawCircle(xElbow, yElbow, 10f, paint)
            canvas.drawCircle(xWrist, yWrist, 10f, paint)
            canvas.drawCircle(xIndex, yIndex, 10f, paint)
            canvas.drawCircle(xThumb, yThumb, 10f, paint)
            canvas.drawCircle(xPinky, yPinky, 10f, paint)

            canvas.drawLine(xShouder, yShouder, xElbow, yElbow, paint)
            canvas.drawLine(xElbow, yElbow, xWrist, yWrist, paint)
            canvas.drawLine(xWrist, yWrist, xIndex, yIndex, paint)
            canvas.drawLine(xWrist, yWrist, xThumb, yThumb, paint)
            canvas.drawLine(xWrist, yWrist, xPinky, yPinky, paint)

            var xShouder1 = rightShoulder.position.x
            var yShouder1 = rightShoulder.position.y
            //canvas.drawCircle(xShouder, yShouder, 10f, paint)
            var xElbow1 = rightElbow.position.x
            var yElbow1 = rightElbow.position.y
            var xWrist1 = rightWrist.position.x
            var yWrist1 = rightWrist.position.y
            var xPinky1 = rightPinky.position.x
            var yPinky1 = rightPinky.position.y
            var xIndex1 = rightIndex.position.x
            var yIndex1 = rightIndex.position.y
            var xThumb1 = rightThumb.position.x
            var yThumb1 = rightThumb.position.y

            canvas.drawCircle(xShouder1, yShouder1, 10f, paint)
            canvas.drawCircle(xElbow1, yElbow1, 10f, paint)
            canvas.drawCircle(xWrist1, yWrist1, 10f, paint)
            canvas.drawCircle(xIndex1, yIndex1, 10f, paint)
            canvas.drawCircle(xThumb1, yThumb1, 10f, paint)
            canvas.drawCircle(xPinky1, yPinky1, 10f, paint)

            canvas.drawLine(xShouder1, yShouder1, xElbow1, yElbow1, paint)
            canvas.drawLine(xElbow1, yElbow1, xWrist1, yWrist1, paint)
            canvas.drawLine(xWrist1, yWrist1, xIndex1, yIndex1, paint)
            canvas.drawLine(xWrist1, yWrist1, xThumb1, yThumb1, paint)
            canvas.drawLine(xWrist1, yWrist1, xPinky1, yPinky1, paint)

            //img.setImageBitmap(null);
            img.setImageBitmap(dstBitmap)

            /*val allPoseLandmarks = pose.getAllPoseLandmarks()
            for (item in allPoseLandmarks) {
                val xPos = item.position.x
                val yPos = item.position.y
                canvas.drawCircle(xPos, yPos, 10f, paint)
            }

            img.setImageBitmap(dstBitmap)*/

            /*fun drawLine(
                canvas: Canvas,
                startLandmark: PoseLandmark?,
                endLandmark: PoseLandmark?,
                paint: Paint
            ){}

            drawLine(canvas, leftShoulder, leftElbow, paint)
            drawLine(canvas, leftElbow, leftWrist, paint)*/
        }
        btn.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                poseDetector.process(image)
                    .addOnSuccessListener { pose ->
                        render(pose)
                        // Task completed successfully
                        Toast.makeText(baseContext, "偵測成功", Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener { e ->
                        // Task failed with an exception
                        Toast.makeText(
                            baseContext, "抱歉，發生錯誤！",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

            }
        })
    }
}