<script setup>
import { ref } from "vue";

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

const comment = ref(props.comment)

defineExpose({
  comment
})
</script>

<template>
  <div>
    <p>Formfehler</p>
    <textarea :readonly="type === 'readonly'" v-model="comment"></textarea>
  </div>
</template>

<style scoped lang="scss">

</style>