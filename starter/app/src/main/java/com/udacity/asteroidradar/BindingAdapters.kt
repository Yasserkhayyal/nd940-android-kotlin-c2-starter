package com.udacity.asteroidradar

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) = with(imageView) {
    if (isHazardous) {
        setImageResource(R.drawable.ic_status_potentially_hazardous)
        contentDescription =
            imageView.context.getString(R.string.potentially_hazardous_icon_ada_label)
    } else {
        setImageResource(R.drawable.ic_status_normal)
        contentDescription = imageView.context.getString(R.string.not_hazardous_icon_ada_label)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) = with(imageView) {
    if (isHazardous) {
        setImageResource(R.drawable.asteroid_hazardous)
        contentDescription =
            imageView.context.getString(R.string.potentially_hazardous_asteroid_image_ada_label)
    } else {
        setImageResource(R.drawable.asteroid_safe)
        contentDescription =
            imageView.context.getString(R.string.not_hazardous_asteroid_image_ada_label)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter(value = ["imageOfTheDayUrl", "imageOfTheDayDay"], requireAll = true)
fun bindImageViewToImageOfTheDay(imageView: ImageView, url: String?, day: String) {
    Picasso.get().load(url).placeholder(R.drawable.placeholder_picture_of_day)
        .error(R.drawable.picture_of_the_day_failed).into(object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) =
                with(imageView) {
                    setImageBitmap(bitmap)
                    contentDescription = String.format(
                        context.getString(R.string.nasa_picture_of_day_content_description_format),
                        day
                    )
                }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) = with(imageView) {
                setImageDrawable(errorDrawable)
                contentDescription =
                    context.getString(R.string.this_is_nasa_s_picture_of_day_showing_nothing_yet)
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) = with(imageView) {
                setImageDrawable(placeHolderDrawable)
                contentDescription =
                    context.getString(R.string.this_is_nasa_s_picture_of_day_showing_nothing_yet)
            }

        })
}
