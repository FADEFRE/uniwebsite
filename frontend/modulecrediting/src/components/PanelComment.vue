<!--
comment input
props:
  - type (may be 'edit' or 'readonly')
  - comment (optional)
exposes:
  - comment
displays:
  - commentary heading
  - textarea element
-->

<script setup>
import { ref } from 'vue';

const props = defineProps({
  type: {
    required: true,
    type: String,
    validator(value) {
      return ['edit', 'readonly'].includes(value)
    }
  },
  comment: {
    type: String
  }
})

const comment = ref('');
if (props.comment) {
  comment.value = props.comment
}

defineExpose({
  comment
})
</script>

<template>
  <div>
    <h3>Kommentar</h3>
    <textarea
        :readonly="type === 'new' || type === 'edit'"
        rows="3"
        v-model="comment"
    />
  </div>
</template>

<style scoped>

</style>
