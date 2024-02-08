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
  readonly: {
    required: true,
    Boolean
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
    <textarea :readonly="readonly" rows="3" v-model="comment"/>
  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.panel-comment {
  @include panelComponent();
}
</style>
