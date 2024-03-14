<script setup>
import { ref, watch } from "vue";
import CustomDropdown from "@/components/util/CustomDropdown.vue";

const props = defineProps(['modelValue','invalid'])
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
    <CustomDropdown
        placeholder="Rolle"
        :options="roleOptions"
        option-value="value"
        option-label="label"
        v-model="role"
        :class="{'invalid': invalid}"
    />
  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.role-dropdown-container {
  width: 17rem;
}
</style>