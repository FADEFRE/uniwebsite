<script setup>
import { ref } from "vue";

/*
formal rejection block with comment field
 */

const props = defineProps({
  /* controls if editable */
  readonly: {
    required: true,
    type: Boolean
  },
  /* comment on formal rejection */
  comment: {
    type: String
  }
})

const comment = ref(props.comment)

defineExpose({
  /* String comment */
  comment
})
</script>

<template>
  <div class="panel-formal-rejection-block">
    <div class="formal-rejection-container">
      <p class="overview-text">{{ $t('PanelFormalRejectionBlock.FormalRejection') }}</p>
    </div>
    <textarea :placeholder="$t('PanelFormalRejectionBlock.Cause')" :readonly="readonly" v-model="comment" />
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