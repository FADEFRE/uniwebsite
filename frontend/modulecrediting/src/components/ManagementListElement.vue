<script setup>
import TrashIcon from "../assets/icons/TrashIcon.vue";
import { ref } from "vue";

const props = defineProps({
  name: {
    required: true,
    type: String,
  },
  code: {
    type: String
  },
  deleteCallback: {
    required: true,
    type: Function
  }
})
</script>

<template>
  <div>

    <div class="module-text-container">
      <p>{{ name }}</p>
      <small v-if="code">{{ code }}</small>
    </div>

    <div class="icons-container">
      <img src="@/assets/icons/EditIcon.svg" class="edit-icon">
      <div class="trash-icon-container" @click="deleteCallback(name)">
        <TrashIcon />
      </div>
    </div>

  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.module-text-container {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.icons-container {
  display: flex;
  align-items: center;
  gap: spacing(s);
}

.trash-icon-container {
  @include smallHighlightBox();
  transition: 0.1s ease-in-out;

  &:hover {
    background-color: $gray-hover;
  }
}

.edit-icon {
  @include smallHighlightBox();
  transition: 0.1s ease-in-out;

  &:hover {
    background-color: $gray-hover;
  }
}
</style>