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

const nameEmpty = ref(false)
const codeEmpty = ref(false)

const triggerEdit = () => {
  // checking name
  nameEmpty.value = !name.value
  // optionally checking code
  if (props.code) {
    codeEmpty.value = !code.value
  }
  // making request if nothing empty
  if (!nameEmpty.value && (!props.code || !codeEmpty.value)) {
    props.editCallback(props.name, name.value, code.value)
  }
}
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
      <InputText type="text" v-model="name" :class="{ 'invalid': nameEmpty }" />
      <small v-if="nameEmpty" class="invalid-text">Name darf nicht leer sein</small>
      <InputText v-if="props.code" type="text" v-model="code" :class="{ 'invalid': codeEmpty }" />
      <small v-if="props.code && codeEmpty" class="invalid-text">Code darf nicht leer sein</small>
      <Button @click="triggerEdit">Speichern</Button>
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