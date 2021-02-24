package org.mongo.visualmongopro.aniket.models

/**
 * A collection contains many documents
 */
data class FlexibleCollection(
  val document: List<Document>
)

/**
 * Every document contains a list of fields
 */
data class Document(
  val fields : List<DocumentField>
)

/**
 * Values can be anything but since null values are deletes, we don't make space in the schema for them
 */
data class DocumentField(
  val name : String,
  val type : MdbTypes,
  val value : Any
)

