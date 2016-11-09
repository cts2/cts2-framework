package edu.mayo.cts2.framework.core.util

import edu.mayo.cts2.framework.model.util.ModelUtils
import org.junit.Test

import static org.junit.Assert.assertEquals

class EncodingUtilsTest {

    @Test
    void testDecodeScopedEntityName() {
        def result = EncodingUtils.decodeEntityName("test:name")

        assertEquals "test", result.getNamespace()
        assertEquals "name", result.getName()
    }

    @Test
    void testDecodeScopedEntityNameWithEncodedColon() {
        def result = EncodingUtils.decodeEntityName("test:some%3Aname")

        assertEquals "test", result.getNamespace()
        assertEquals "some:name", result.getName()
    }

    @Test
    void testDecodeScopedEntityNameWithEncodedColonAndDefaultNamespace() {
        def result = EncodingUtils.decodeEntityName("test:some%3Aname", "test")

        assertEquals "test", result.getNamespace()
        assertEquals "some:name", result.getName()
    }

    @Test
    void testDecodeScopedEntityNameWithEncodedColonAndDefaultNamespaceWithoutNamespace() {
        def result = EncodingUtils.decodeEntityName("some%3Aname", "test")

        assertEquals "test", result.getNamespace()
        assertEquals "some:name", result.getName()
    }

    @Test
    void testEncodeScopedEntityName() {
        def result = EncodingUtils.encodeScopedEntityName(ModelUtils.createScopedEntityName("some:name", "test"))

        assertEquals "test:some%253Aname", result
    }

    @Test
    void testEncodeEntityName() {
        def result = EncodingUtils.encodeEntityName("some:name")

        assertEquals "some%253Aname", result
    }

}
