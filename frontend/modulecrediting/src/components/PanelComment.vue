<!--
comment input
props:
  - type (may be 'new', 'edit' or 'readonly')
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
      return ['new', 'edit', 'readonly'].includes(value)
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
  <div class="panel-comment">
    <h4>Kommentar</h4>
    <textarea>
        :readonly="!(type === 'new' || type === 'edit')"
        rows="3"
        v-model="comment"
    </textarea>
  </div>
</template>

<style scoped lang="scss">
@import '@/assets/mixins.scss';
@import '@/assets/variables.scss';

.panel-comment {
  @include panelComponent();
}
</style>
