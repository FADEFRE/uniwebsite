<script setup>
import EditIcon from "@/assets/icons/EditIcon.vue";
import TrashIcon from "@/assets/icons/TrashIcon.vue";
import ButtonLink from "@/components/ButtonLink.vue";
import { ref } from "vue";

const props = defineProps({
  name: {
    required: true,
    type: String
  },
  code: {
    type: String
  },
  showCode: {
    type: Boolean,
    default: true
  },
  editCallback: {
    required: true,
    type: Function
  },
  deleteCallback: {
    required: true,
    type: Function
  },
  existsText : {
    type: String,
    default: ""
  }
})

const dialogVisible = ref(false)
const name = ref(props.name)
const code = ref(props.code || '')

const nameEmpty = ref(false)
const exists = ref(false)

const clearDialogData = () => {
  name.value = props.name
  code.value = props.code || ''
  nameEmpty.value = false
}

const triggerEdit = () => {
  // resetting exists
  exists.value = false
  // checking name
  nameEmpty.value = !name.value
  // making request if nothing empty
  if (!nameEmpty.value) {
    props.editCallback(exists, props.name, name.value, code.value)
  }
}
</script>

<template>
  <div class="list-item">

    <div class="module-text-container">
      <p>{{ props.name }}</p>
      <small v-if="props.code">{{ props.code }}</small>
    </div>

    <div class="icons-container">
      <EditIcon @click="dialogVisible = !dialogVisible"/>
      <TrashIcon @click="deleteCallback(props.name)"/>
    </div>

    <Dialog modal :dismissable-mask="true" :draggable="false" v-model:visible="dialogVisible" header="Bearbeiten" @hide="clearDialogData">
      <InputText type="text" placeholder="Name" v-model="name"
                 :class="{ 'invalid': nameEmpty || exists }" class="white" />
      <small v-if="nameEmpty" class="invalid-text">Name darf nicht leer sein</small>
      <InputText v-if="showCode" type="text" placeholder="Code" v-model="code"
                 :class="{ 'invalid': exists }" />
      <small v-if="exists" class="invalid-text">{{ existsText }}</small>
      <ButtonLink @click="triggerEdit">Speichern</ButtonLink>
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

.list-item {
  @include smallHighlightBox();
  @include verticalListItem($gray);
  width: 100%;
}

.icons-container {
  display: flex;
  align-items: center;
  gap: spacing(s);
}
</style>