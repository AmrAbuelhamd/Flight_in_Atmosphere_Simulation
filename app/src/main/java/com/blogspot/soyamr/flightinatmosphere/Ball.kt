package com.blogspot.soyamr.flightinatmosphere

import kotlin.math.cos
import kotlin.math.round
import kotlin.math.sin
import kotlin.math.sqrt

class Ball {
    private var cosA = 0.0
    private var sinA = 0.0
    private var height = 0.0
    private var weight = 0.0
    private var k = 0.0
    private var size = 0.0
    private var speed = 0.0
    private var angle = 0.0
        set(value) {
            field = Math.toRadians(value)
        }

    private var v0x = 0.0
    private var v0y = 0.0

    var x = 0.0
        private set
    var y = 0.0
        private set

//    var maximumHeight = 0.0
//    var maximumWidth = 0.0

    var t = 0.0

    fun reset(speed: Double, height: Double, angle: Double, size: Double, weight: Double) {
        this.speed = speed
        this.y = height
        this.angle = angle
        this.size = size
        this.weight = weight
        t = 0.0
        x = 0.0
        y = height
        this.height = height
        cosA = cos(this.angle)
        sinA = sin(this.angle)

        v0y = sinA * speed
        v0x = cosA * speed

        k = 0.5 * C * ROH * size / weight

//        maximumHeight = (v0y * v0y) / (2.0 * G) + height + 1
//        val totalT = (v0y + sqrt(v0y * v0y + 2.0 * G * (maximumHeight))) / G
//        maximumWidth = totalT * v0x + 1
    }

    //https://stackoverflow.com/questions/53234843/kotlin-sumbydouble-returning-additional-decimals
    fun update() {
        t += DeltaT
        t = round(t * 100) / 100;
        val v: Double = sqrt(v0x * v0x + v0y * v0y)
        v0x -= k * v0x * v * DeltaT;
        v0y -= (G + k * v0y * v) * DeltaT;
        x += v0x * DeltaT;
        y += v0y * DeltaT;
    }

}