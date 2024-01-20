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
  <div class="panel-formal-rejection-block">
    <div class="formal-rejection-container">
      <p class="formal-rejection-text">Formfehler</p>
    </div>
    <textarea :readonly="type === 'readonly'" v-model="comment"></textarea>
  </div>
</template>

<style scoped lang="scss">
@import '@/assets/mixins.scss';
@import '@/assets/variables.scss';

.panel-formal-rejection-block {
  display: flex;
  width: 100%;
  align-self: stretch;

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

.formal-rejection-text {
  font-family: "Jost";
  text-transform: uppercase;
  text-decoration: none;
  width: max-content;

  color: $white;
  font-size: 1.125rem;
  font-weight: 600;




}

textarea {
  width: 100%;
  resize: none;
  border: none;
}
</style>