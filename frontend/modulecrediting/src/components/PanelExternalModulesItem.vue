<!--
single external module
props:
  - allowTextEdit (toggles InputText elements readonly)
  - allowFileEdit (toggles FileInput elements readonly)
  - allowDelete
  - moduleName
  - university
  - points
  - pointSystem
  - selectedFile
emits:
  - deleteSelf (called on trash button click)
exposes:
  - moduleName
  - university
  - points
  - pointSystem
  - selectedFile
displays:
  - input fields for moduleName, university, creditPoints, pointSystem
  - drop zone for module description
  - delete icon (if allowDelete)
-->

<script setup>
import { ref, computed, watch, onBeforeMount } from "vue";
import FileInput from "@/components/FileInput.vue";

const props = defineProps({
  allowTextEdit: {
    required: true,
    type: Boolean
  },
  allowFileEdit: {
    required: true,
    type: Boolean
  },
  allowDelete: {
    required: true,
    type: Boolean,
  },
  id: {
    type: Number,
  },
  name: {
    type: String
  },
  university: {
    type: String
  },
  points: {
    type: Number
  },
  pointSystem: {
    type: String
  },
  selectedFile: {
    type: Object
  }
})

const emit = defineEmits(['deleteSelf', 'change'])

const id = props.id ? props.id : undefined

const name = ref(props.name || "")
const university = ref(props.university || "")
const points = ref(props.points || "")
const pointSystem = ref(props.pointSystem || "")

const fileInput = ref()
const selectedFile = computed(() => fileInput.value?.selectedFile)

watch([name, university, points, pointSystem], () => emit('change'))

defineExpose({
  id, name, university, points, pointSystem, selectedFile
})
</script>

<template>
  <div class="external-modules-item">
    <div class="screen-split">
      <div class="left-side">
        <InputText :readonly="!allowTextEdit" type="text" placeholder="Modulname" v-model="name" />
        <InputText :readonly="!allowTextEdit" type="text" placeholder="Universität" v-model="university" />
      </div>

      <div class="right-side">

        <div class="point-container">
          <InputText :readonly="!allowTextEdit" type="text" placeholder="Punkte" v-model="points" />
          <InputText :readonly="!allowTextEdit" type="text" placeholder="Punktesystem" v-model="pointSystem" />
        </div>

        <FileInput :readonly="!allowFileEdit" :selected-file="props.selectedFile" ref="fileInput" />
      </div>

    </div>

    <div v-if="allowDelete">
      <img src="../assets/icons/Trash.svg" @click="emit('deleteSelf')" class="trash-icon">
    </div>

  </div>
</template>

<style scoped lang="scss">
@import '../assets/mixins.scss';
@import '../assets/variables.scss';


.external-modules-item {
  @include verticalListItem($gray);

  width: 100%;
  padding: 0.625rem 0.5rem 0.625rem 1.25rem;

  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  gap: 0.5rem;
}

.screen-split {
  @include screenSplit();

  @media only screen and (max-width: 700px) {
    flex-wrap: wrap;
  }
}

.left-side {
  @include verticalList(small);
  justify-content: space-between;
  align-self: stretch;
  width: 50%;
  
  @media only screen and (max-width: 700px) {
    width: 100%;
  }
}

:deep(.p-inputtext:hover) {
  background-color: $gray-hover;
}

.right-side {
  @include verticalList(small);
  justify-content: space-between;
  align-self: stretch;
  width: 50%;

  @media only screen and (max-width: 700px) {
    width: 100%;
  }
}

.point-container {
  display: flex;
  gap: 0.625rem;
  & .p-inputtext {
    width: 100%;
  }
}

.trash-icon {
  @include trashIconAnimation();
  padding:  0.5rem 0.5rem;
  &:hover {
    background-color: $gray-hover;
  }
}
</style>