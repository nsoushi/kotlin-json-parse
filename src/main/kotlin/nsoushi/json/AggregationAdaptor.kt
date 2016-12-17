package nsoushi.json

import com.squareup.moshi.*
import java.lang.reflect.Type

/**
 *
 * @author nsoushi
 */
class AggregationAdapter : JsonAdapter<Aggregations>() {

    companion object {
        val FACTORY: Factory = object : Factory {
            override fun create(type: Type, annotations: Set<Annotation>, moshi: Moshi): JsonAdapter<*>? {
                if (type === Aggregations::class.java) {
                    return AggregationAdapter()
                }
                return null
            }
        }
    }

    @ToJson
    override fun toJson(writer: JsonWriter?, value: Aggregations?) {

        if (writer == null || value == null)
            throw JsonDataException("json parse error")

        writer.setIndent("    ")

        writer.beginObject()
        writer.name(value.name)
        writeAggregations(writer, value)
        writer.endObject()
    }

    fun writeAggregations(writer: JsonWriter, value: Aggregations) {

        writer.beginObject()

        writer.name("terms")
        writeTerms(writer, value)

        if (value.aggregations != null) {
            writer.name("aggregations")
            toJson(writer, value.aggregations)
        } else {
            writer.name("aggregations").nullValue()
        }

        writer.endObject()
    }

    fun writeTerms(writer: JsonWriter, value: Aggregations) {
        writer.beginObject()
        writer.name("field").value(value.terms.field)
        writer.name("size").value(value.terms.size)
        writer.endObject()
    }

    @FromJson
    override fun fromJson(reader: JsonReader?): Aggregations? {

        reader ?: throw JsonDataException("json parse error")

        var aggregations: Aggregations? = null

        reader.beginObject()
        while (reader.hasNext()) {
            aggregations = readAggregations(reader.nextName(), reader)
        }
        reader.endObject()

        return aggregations
    }

    fun readAggregations(name: String, reader: JsonReader): Aggregations {

        var terms: Terms? = null
        var subAaggregations: Aggregations? = null

        reader.beginObject()
        while (reader.hasNext()) {
            val nextName = reader.nextName()
            if (nextName == "terms")
                terms = readTerms(reader)
            else if (nextName == "aggregations") {
                if (reader.peek() != JsonReader.Token.NULL) {
                    subAaggregations = fromJson(reader)
                } else {
                    reader.skipValue()
                }
            } else
                reader.skipValue()
        }
        reader.endObject()

        return Aggregations(name, terms as Terms, subAaggregations)
    }

    fun readTerms(reader: JsonReader): Terms {

        var field: String? = null
        var size: Long? = null

        reader.beginObject()
        while (reader.hasNext()) {
            val name = reader.nextName()
            if (name == "field")
                field = reader.nextString()
            else if (name == "size")
                size = reader.nextLong()
            else
                reader.skipValue()
        }
        reader.endObject()
        return Terms(field as String, size as Long)
    }
}

data class Aggregations(
        val name: String,
        val terms: Terms,
        val aggregations: Aggregations?
)

data class Terms(
        val field: String,
        val size: Long
)
