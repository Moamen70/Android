package com.udacity.asteroidradar

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.main.MainAdabter


@BindingAdapter("asteroidsList")
fun bindAsteroidsList(recyclerView: RecyclerView, list: List<Asteroid>?) {

    val adapter = recyclerView.adapter as MainAdabter
    adapter.submitList(list)
}

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("setPhotoOfDay")
fun bindPictureOfDay(imageView: ImageView, pictureOfDay: PictureOfDay?) {

    val imageUrl = pictureOfDay?.url
    imageUrl.let {
        val imgUri = imageUrl?.toUri()?.buildUpon()?.scheme("https")?.build()
        Picasso.with(imageView.context)
            .load(imgUri)
            .placeholder(R.drawable.placeholder_picture_of_day)
            .error(R.drawable.placeholder_picture_of_day)
            .into(imageView)
    }
    if (pictureOfDay != null) {
        imageView.contentDescription =
            imageView.context.getString(R.string.image_of_the_day) + " " + pictureOfDay.title
    }
}

@BindingAdapter("setPhotoTitle")
fun bindPictureTitle(textView: TextView, title: String?) {
    if (title != null) {
        textView.text = title
        textView.contentDescription = title
    } else {
        textView.text = textView.context.getString(R.string.image_of_the_day)
        textView.contentDescription = textView.context.getString(R.string.image_of_the_day)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
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
