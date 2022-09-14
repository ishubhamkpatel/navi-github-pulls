package com.navi.img

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation

fun ImageView.loadImage(
    url: String?,
    crossFadeAnim: Boolean = false,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = null,
    @Shape shape: Int = 0,
    successListener: ((String) -> Unit)? = null,
    failureListener: ((String?) -> Unit)? = null
) {
    this.load(url) {
        crossfade(crossFadeAnim)
        placeholder?.let { placeholder(it) }
        error?.let { error(it) }
        with(
            when (shape) {
                Shape.Circle -> CircleCropTransformation()
                Shape.RoundedCorners -> RoundedCornersTransformation()
                else -> null
            }
        ) {
            this?.let { transformations(it) }
        }
        if ((successListener != null) or (failureListener != null)) {
            listener(
                onSuccess = { _, result -> successListener?.invoke(result.dataSource.name) },
                onError = { _, result -> failureListener?.invoke(result.throwable.message) }
            )
        }
    }
}

@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.TYPE_PARAMETER,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.FIELD,
    AnnotationTarget.LOCAL_VARIABLE
)
@IntDef(Shape.Circle, Shape.RoundedCorners)
annotation class Shape {
    companion object {
        const val Circle = 1
        const val RoundedCorners = 2
    }
}