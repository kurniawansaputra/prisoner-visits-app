package com.myappkunjungan.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class VisitorResponse(

	@field:SerializedName("data")
	val data: List<Visitor>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
): Parcelable

@Parcelize
data class Visitor(

	@field:SerializedName("nik")
	val nik: String,

	@field:SerializedName("address")
	val address: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("date_visited")
	val dateVisited: String,

	@field:SerializedName("prisoner_number")
	val prisonerNumber: String,

	@field:SerializedName("luggage")
	val luggage: String,
): Parcelable
