<script setup>
import ArrowIcon from "@/assets/icons/ArrowIcon.vue";
import { ref, watch } from "vue";

const props = defineProps(['modelValue'])
const emit = defineEmits(['update:modelValue'])

const roleOptions = [
  { value: 'ROLE_STUDY', label: 'STUDIENBUERO' },
  { value: 'ROLE_CHAIR', label: 'PRUEFUNGSAUSSCHUSS' },
  { value: 'ROLE_ADMIN', label: 'ADMIN' }
]
const role = ref(props.modelValue)

watch(role, (newRole) => emit('update:modelValue', newRole))
</script>

<template>
  <div class="role-dropdown-container">
    <Dropdown placeholder="Rolle" v-model="role" :options="roleOptions" option-value="value" option-label="label">
      <template #dropdownicon>
        <ArrowIcon color="white" direction="down"/>
      </template>
    </Dropdown>
  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.role-dropdown-container {
  width: 16rem;
}
</style>