package com.example.videoframes
}
}.start()
}
}


override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
super.onActivityResult(requestCode, resultCode, data)
if (requestCode == PICK_VIDEO && resultCode == Activity.RESULT_OK) {
videoUri = data?.data
Toast.makeText(this, "Đã chọn video", Toast.LENGTH_SHORT).show()
}
}


private fun extractFrames(uri: Uri, intervalSec: Int): Int {
var saved = 0
val retriever = MediaMetadataRetriever()
try {
retriever.setDataSource(this, uri)
val durationMs =
retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong()
?: 0L
val outputDir = File(
getExternalFilesDir(Environment.DIRECTORY_PICTURES),
"VideoFrames"
)
if (!outputDir.exists()) outputDir.mkdirs()


var timeMs = 0L
while (timeMs < durationMs) {
val bitmap: Bitmap? = retriever.getFrameAtTime(
timeMs * 1000, // microseconds
MediaMetadataRetriever.OPTION_CLOSEST
)
bitmap?.let { bmp ->
val file = File(outputDir, "frame_${timeMs / 1000}.jpg")
FileOutputStream(file).use { out ->
bmp.compress(Bitmap.CompressFormat.JPEG, 90, out)
}
saved++
}
timeMs += (intervalSec * 1000).toLong()
}
} catch (e: Exception) {
e.printStackTrace()
} finally {
retriever.release()
}
return saved
}
}