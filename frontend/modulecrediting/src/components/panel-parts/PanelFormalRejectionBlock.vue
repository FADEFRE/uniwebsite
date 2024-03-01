<script setup>
import { ref } from "vue";

const props = defineProps({
  readonly: {
    required: true,
    type: Boolean
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
  <div class="panel-formal-rejection-block">
    <div class="formal-rejection-container">
      <p class="overview-text">{{ $t('PanelFormalRejectionBlock.FormalMistake') }}</p>
    </div>
    <textarea :readonly="readonly" v-model="comment"></textarea>
  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.panel-formal-rejection-block {
  display: flex;
  width: 100%;
  align-self: stretch;
  height: rem(121px);

  border: 2px solid $dark-gray;
}

.formal-rejection-container {
  @include smallHighlightBox();
  display: flex;
  align-self: stretch;
  align-items: center;
  background-color: $red;

  border-right: 2px solid $dark-gray;
}

</style>