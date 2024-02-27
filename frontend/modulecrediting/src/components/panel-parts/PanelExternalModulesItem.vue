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
import { ref, computed, watch } from "vue";
import FileInput from "@/components/util/FileInput.vue";
import TrashIcon from "@/assets/icons/TrashIcon.vue";

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

const validatePointInput = () => {
  console.log(points.value)
  console.log(points.value.replace(/[^0-9]/g, ''))
  points.value = points.value.replace(/[^0-9]/g, '')
}

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
          <InputText :readonly="!allowTextEdit" type="text" placeholder="Modulname" v-model="name"
            :class="{ 'invalid': !nameValid }" class="gray"/>
          <small v-if="!nameValid" class="invalid-text">Modulname darf nicht leer sein</small>
        </div>
        <div class="input-container">
          <InputText :readonly="!allowTextEdit" type="text" placeholder="Universität" v-model="university"
            :class="{ 'invalid': !universityValid }" class="gray" />
          <small v-if="!universityValid" class="invalid-text">Universität darf nicht leer sein</small>
        </div>
      </div>

      <div class="right-side">

        <div class="point-container">
          <div class="input-container">
            <InputText :readonly="!allowTextEdit" type="text" placeholder="Punkte" v-model="points"
              @input.prevent="validatePointInput" :class="{ 'invalid': !pointsValid }" class="gray"/>
            <small v-if="!pointsValid" class="invalid-text">Punkte müssen als Zahl angegeben werden</small>
          </div>

          <div class="input-container">
            <InputText :readonly="!allowTextEdit" type="text" placeholder="Punktesystem" v-model="pointSystem"
              :class="{ 'invalid': !pointSystemValid }" class="gray"/>
            <small v-if="!pointSystemValid" class="invalid-text">Punktsystem darf nicht leer sein</small>
          </div>

        </div>

        <FileInput :readonly="!allowFileEdit" type="pdf" :selected-file="props.selectedFile" ref="fileInput">
          Modulbeschreibung auswählen
        </FileInput>
      </div>

    </div>
    <TrashIcon v-if="allowDelete" @click="emit('deleteSelf')" background-size="small"
               :aria-label="`Externes Modul ${name || 'ohne Namen'} löschen`"/>
  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;


.external-modules-item {
  @include verticalListItem($gray);

  width: 100%;
  padding: spacing(s);

  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  gap: spacing(s);
}

.screen-split {
  @include screenSplit();

  @include breakpoint(s) {
    flex-wrap: wrap;
    flex-direction: column;
  }
}

.left-side {
  @include verticalList(s);
  justify-content: space-between;
  align-self: stretch;
  width: 50%;

  @include breakpoint(s) {
    width: 100%;
  }
}

.right-side {
  @include verticalList(s);
  justify-content: space-between;
  align-self: stretch;
  width: 50%;

  @include breakpoint(s) {
    width: 100%;
  }
}

.point-container {
  display: flex;
  gap: spacing(s);
}


</style>