<script setup>
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
        <img src="../assets/icons/ArrowWhite.svg">
      </template>
    </Dropdown>
  </div>
</template>

<style scoped lang="scss">
.role-dropdown-container {
  width: 16rem;
}
</style>