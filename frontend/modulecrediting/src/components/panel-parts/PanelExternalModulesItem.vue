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
  points: {
    type: Number
  },
  pointSystem: {
    type: String
  },
  university: {
    type: String
  },
  externalCourse: {
    type: String
  },
  selectedFile: {
    type: Object
  }
})

const emit = defineEmits(['deleteSelf', 'change'])

const id = props.id ? props.id : undefined

const name = ref(props.name || "")
const points = ref(props.points || "")
const pointSystem = ref(props.pointSystem || "")
const university = ref(props.university || "")
const externalCourse = ref(props.externalCourse || "")

const validatePointInput = () => {
  points.value = points.value.replace(/[^0-9]/g, '')
}

const fileInput = ref()
const selectedFile = computed(() => fileInput.value?.selectedFile)

watch([name, university, points, pointSystem, selectedFile], () => emit('change'))

const nameValid = ref(true)
const pointsValid = ref(true)
const pointSystemValid = ref(true)
const universityValid = ref(true)
const externalCourseValid = ref(true)

const numberRegExp = new RegExp('\\d+')

const checkValidity = () => {
  // setting validity
  nameValid.value = Boolean(name.value)
  pointsValid.value = numberRegExp.test(points.value)
  pointSystemValid.value = Boolean(pointSystem.value)
  universityValid.value = Boolean(university.value)
  externalCourseValid.value = Boolean(externalCourse.value)
  // cascading calls
  const fileValid = fileInput.value.checkValidity()
  // return
  return nameValid.value && pointsValid.value && pointSystemValid.value && universityValid.value && externalCourseValid.value && fileValid.value
}

defineExpose({
  id, name, points, pointSystem, university, externalCourse, selectedFile, checkValidity
})
</script>

<template>
  <div class="external-modules-item">

    <div class="item-content">
    <div class="screen-split">

      <div class="input-container left-side">
        <InputText :readonly="!allowTextEdit" type="text" :placeholder="$t('PanelExternalModulesItem.ModuleName')" v-model="name"
          :class="{ 'invalid': !nameValid }" class="gray" />
        <small v-if="!nameValid" class="invalid-text">{{ $t('PanelExternalModulesItem.NameEmpty') }}</small>
      </div>

      <div class="point-container right-side">
        <div class="input-container">
          <InputText :readonly="!allowTextEdit" type="text" :placeholder="$t('PanelExternalModulesItem.Credits')" v-model="points"
            @input.prevent="validatePointInput" :class="{ 'invalid': !pointsValid }" class="gray" />
          <small v-if="!pointsValid" class="invalid-text">{{ $t('PanelExternalModulesItem.PointsNum') }}</small>
        </div>

        <div class="input-container">
          <InputText :readonly="!allowTextEdit" type="text" :placeholder="$t('PanelExternalModulesItem.CreditingSystem')" v-model="pointSystem"
            :class="{ 'invalid': !pointSystemValid }" class="gray" />
          <small v-if="!pointSystemValid" class="invalid-text">{{ $t('PanelExternalModulesItem.CredSysEmpty') }}</small>
        </div>
      </div>

    </div>

    <div class="screen-split">

      <div class="input-container">
        <InputText :readonly="!allowTextEdit" type="text" :placeholder="$t('PanelExternalModulesItem.University')" v-model="university"
          :class="{ 'invalid': !universityValid }" class="gray" />
        <small v-if="!universityValid" class="invalid-text">{{ $t('PanelExternalModulesItem.UniEmpty') }}</small>
      </div>

      <div class="input-container">
        <InputText :readonly="!allowTextEdit" type="text" :placeholder="$t('PanelExternalModulesItem.Course')" v-model="externalCourse"
          :class="{ 'invalid': !externalCourseValid }" class="gray" />
        <small v-if="!externalCourseValid" class="invalid-text">{{ $t('PanelExternalModulesItem.ExtCourseEmpty')}}</small>
      </div>

    </div>

    <FileInput :readonly="!allowFileEdit" type="pdf" :selected-file="props.selectedFile" ref="fileInput">
      {{ $t('PanelExternalModulesItem.ChooseModuleDescription') }}
    </FileInput>
  </div>

    <TrashIcon v-if="allowDelete" @click="emit('deleteSelf')" background-size="small"
      :aria-label="`${$t('PanelExternalModulesItem.AriaExtModule')} ${name || 'ohne Namen'} ${$t('PanelExternalModulesItem.AriaDelete')}`" /> <!-- TODO: missing translation-->
  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;


.external-modules-item {
  @include verticalListItem($gray);
  width: 100%;
  padding: spacing(m);

  display: flex;
  align-items: center;
  gap: spacing(s);
}

.item-content {
  @include verticalList(s);
}

.screen-split {
  @include screenSplit();
  gap: spacing(s);

  @include breakpoint(s) {
    flex-wrap: wrap;
    flex-direction: column;
  }
}

.left-side {
  width: 50%;

  @include breakpoint(s) {
    width: 100%;
  }
}

.right-side {
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