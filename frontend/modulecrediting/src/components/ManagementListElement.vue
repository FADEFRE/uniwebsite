<script setup>
import TrashIcon from "../assets/icons/TrashIcon.vue";
import { ref } from "vue";

const props = defineProps({
  name: {
    required: true,
    type: String
  },
  code: {
    type: String
  },
  editCallback: {
    required: true,
    type: Function
  },
  deleteCallback: {
    required: true,
    type: Function
  }
})

const dialogVisible = ref(false)
const name = ref(props.name)
const code = ref(props.code)
</script>

<template>
  <div>

    <div class="module-text-container">
      <p>{{ props.name }}</p>
      <small v-if="props.code">{{ props.code }}</small>
    </div>

    <div class="icons-container">
      <img src="@/assets/icons/EditIcon.svg" class="edit-icon" @click="dialogVisible = !dialogVisible">
      <div class="trash-icon-container" @click="deleteCallback(props.name)">
        <TrashIcon />
      </div>
    </div>

    <Dialog modal :dismissable-mask="true" :draggable="false" v-model:visible="dialogVisible" header="Bearbeiten">
      <InputText type="text" v-model="name" />
      <InputText v-if="props.code" type="text" v-model="code" />
      <Button @click="editCallback(props.name, name, code)">Speichern</Button>
    </Dialog>

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