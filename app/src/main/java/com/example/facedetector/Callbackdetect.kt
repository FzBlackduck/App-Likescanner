package com.example.facedetector

import com.google.mlkit.vision.face.Face

interface Callbackdetect {
       fun callback(face: Face)
}