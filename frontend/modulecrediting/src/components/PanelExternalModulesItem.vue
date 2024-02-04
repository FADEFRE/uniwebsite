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

watch([name, university, points, pointSystem, selectedFile], () => emit('change'))

const nameValid = ref(true)
const universityValid = ref(true)
const pointsValid = ref(true)
const pointSystemValid = ref(true)

const numberRegExp = new RegExp('\\d+')

const checkValidity = () => {
  // setting validity
  nameValid.value = Boolean(name.value)
  universityValid.value = Boolean(university.value)
  pointsValid.value = numberRegExp.test(points.value)
  pointSystemValid.value = Boolean(pointSystem.value)
  // cascading calls
  const fileValid = fileInput.value.checkValidity()
  // return
  return nameValid.value && universityValid.value && pointsValid.value && pointSystemValid.value && fileValid.value
}

defineExpose({
  id, name, university, points, pointSystem, selectedFile, checkValidity
})
</script>

<template>
  <div class="external-modules-item">
    <div class="screen-split">
      <div class="left-side">
        <div class="input-container">
          <InputText :readonly="!allowTextEdit" type="text" placeholder="Modulname" v-model="name" :class="{ 'invalid': !nameValid }" />
          <small v-if="!nameValid" class="invalid-text">Modulname darf nicht leer sein</small>
        </div>
        <div class="input-container">
          <InputText :readonly="!allowTextEdit" type="text" placeholder="Universität" v-model="university" :class="{ 'invalid': !universityValid }" />
          <small v-if="!universityValid" class="invalid-text">Universität darf nicht leer sein</small>
        </div>
      </div>

      <div class="right-side">

        <div class="point-container">
          <div class="input-container">
            <InputText :readonly="!allowTextEdit" type="text" placeholder="Punkte" v-model="points" :class="{ 'invalid': !pointsValid }" />
            <small v-if="!pointsValid" class="invalid-text">Punkte müssen als Zahl angegeben werden</small>
          </div>
          
          <div class="input-container">
            <InputText :readonly="!allowTextEdit" type="text" placeholder="Punktesystem" v-model="pointSystem" :class="{ 'invalid': !pointSystemValid }" />
            <small v-if="!pointSystemValid" class="invalid-text">Punktsystem darf nicht leer sein</small>
          </div>
          
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
  padding: 0.5rem 0.5rem 0.5rem 1.25rem;

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
  gap: 0.5rem;
  & .p-inputtext {
    width: 100%;
  }
}

.input-container {
  display: flex;
  gap: 0.5rem;
  flex-direction: column;
  width: 100%;
}

.trash-icon {
  @include trashIconAnimation();
  padding:  0.5rem 0.5rem;
  &:hover {
    background-color: $gray-hover;
  }
}

.invalid {
  border: 2px solid $red;
}

.invalid-text {
  color: $red;
}
</style>