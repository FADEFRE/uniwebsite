<script setup>
import { ref } from 'vue';

/*
comment section
 */

const props = defineProps({
  /* controls if editable */
  readonly: {
    required: true,
    Boolean
  },
  /* comment to be displayed (initially) */
  comment: {
    type: String
  }
})

const comment = ref('');
if (props.comment) {
  comment.value = props.comment
}

defineExpose({
  /* String comment */
  comment
})
</script>

<template>
  <div class="panel-container">
    <h4 id="commentHeading">{{ $t('PanelComment.Comment') }}</h4>
    <div class="textarea-container">
      <textarea :readonly="readonly" rows="3" v-model="comment" class="white" aria-labelledby="commentHeading" />
    </div>

  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;


.textarea-container {
  border: 2px solid $dark-gray;
  width: 100%;
}
</style>
