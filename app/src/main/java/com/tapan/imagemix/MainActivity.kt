package com.tapan.imagemix

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.bumptech.glide.Glide


class MainActivity : AppCompatActivity() {

    private var mImageView1: ImageView? = null
    private var mImageView2: ImageView? = null
    private var mOutputImageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setInputImage()
        OperationAsyncTask().execute()
    }

    private fun initViews() {

        mImageView1 = findViewById(R.id.iv1)
        mImageView2 = findViewById(R.id.iv2)
        mOutputImageView = findViewById(R.id.outputIv)
    }

    private fun setInputImage() {

        Glide.with(this)
                .load(R.drawable.image1)
                .into(mImageView1!!)

        Glide.with(this)
                .load(R.drawable.image2)
                .into(mImageView2!!)

    }

    private fun average(argb1: Int, argb2: Int): Int {

        return (argb1 and 0xFF) + (argb2 and 0xFF) shr 1 or
                (
                        (argb1 shr 8 and 0xFF) + (argb2 shr 8 and 0xFF) shr 1 shl 8) or
                (
                        (argb1 shr 16 and 0xFF) + (argb2 shr 16 and 0xFF) shr 1 shl 16) or
                (
                        (argb1 shr 24 and 0xFF) + (argb2 shr 24 and 0xFF) shr 1 shl 24)
    }

    private inner class OperationAsyncTask : AsyncTask<String, String, String>() {

        var outputBitmap: Bitmap? = null

        override fun doInBackground(vararg params: String): String? {

            val bitmap1 = BitmapFactory.decodeResource(resources, R.drawable.image1)
            val rgbValues1 = IntArray(bitmap1.width * bitmap1.height)
            bitmap1.getPixels(rgbValues1, 0, bitmap1.width, 0, 0, bitmap1.width, bitmap1.height)

            val bitmap2 = BitmapFactory.decodeResource(resources, R.drawable.image2)
            val rgbValues2 = IntArray(bitmap2.width * bitmap2.height)
            bitmap2.getPixels(rgbValues2, 0, bitmap2.width, 0, 0, bitmap2.width, bitmap2.height)

            val rgbValues = IntArray(bitmap2.width * bitmap2.height)

            for (i in 0 until rgbValues1.size) {

                rgbValues[i] = average(rgbValues1[i], rgbValues2[i])
            }

            outputBitmap = Bitmap.createBitmap(rgbValues, bitmap1.width, bitmap1.height, Bitmap.Config.ARGB_8888)

            return null
        }

        override fun onPostExecute(result: String?) {

            Glide.with(this@MainActivity)
                    .load(outputBitmap)
                    .into(mOutputImageView!!)

        }
    }
}
