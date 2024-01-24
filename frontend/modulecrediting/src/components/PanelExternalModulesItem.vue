<!--
single external module
props:
  - type (may be 'new', 'edit' or 'readonly')
      type 'new' allows editing all data and deleting self
      type 'edit' allows editing all data except selected file
      type 'readonly' allows no editing
  - moduleName (should be given if type is 'edit' or 'readonly')
  - university (should be given if type is 'edit' or 'readonly')
  - points (should be given if type is 'edit' or 'readonly')
  - pointSystem (should be given if type is 'edit' or 'readonly')
  - selectedFile (should be given if type is 'edit' or 'readonly')
  - allowDelete (Boolean, defaults to false)
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
  - delete icon (if type is 'new')
-->

<script setup>
import { ref, computed, watch, onBeforeMount } from "vue";
import FileInput from "@/components/FileInput.vue";

const props = defineProps({
  type: {
    required: true,
    type: String,
    validator(value) {
      return ['new', 'edit', 'readonly'].includes(value)
    }
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
  },
  allowDelete: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['deleteSelf', 'change'])

const id = props.id ? props.id : undefined

// checking props
onBeforeMount(() => {
  const warn = (prop) => {
    console.warn(`PanelExternalModulesItem: prop ${prop} should be given if type is ${props.type}`)
  }

  if (props.type === 'edit' || props.type === 'readonly') {
    if (!props.name) warn('name')
    if (!props.university) warn('university')
    if (!props.points) warn('points')
    if (!props.pointSystem) warn('pointSystem')
    if (!props.selectedFile) warn('selectedFile')
  }
})

const readonly = props.type === 'readonly'

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
        <InputText :readonly="readonly" type="text" placeholder="Modulname" v-model="name" />
        <InputText :readonly="readonly" type="text" placeholder="Universität" v-model="university" />
      </div>

      <div class="right-side">

        <div class="point-container">
          <InputText :readonly="readonly" type="text" placeholder="Punkte" v-model="points" />
          <InputText :readonly="readonly" type="text" placeholder="Punktesystem" v-model="pointSystem" />
        </div>

        <FileInput :readonly="type === 'edit' || type === 'readonly'" :selected-file="props.selectedFile" ref="fileInput" />
      </div>

    </div>

    <div v-if="type === 'new'">
      <img v-if="allowDelete" src="../assets/icons/Trash.svg" @click="emit('deleteSelf')">
    </div>

  </div>
</template>

<style scoped lang="scss">
@import '../assets/mixins.scss';
@import '../assets/variables.scss';


.external-modules-item {
  @include verticalListItem($gray);

  width: 100%;
  padding: 0.625rem 0.9375rem 0.625rem 1.25rem;

  display: flex;
  flex-direction: row;
  justify-content: space-between;
  gap: 0.9375rem;

  &:hover{
    background-color: $mid-gray;
  }
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
</style>