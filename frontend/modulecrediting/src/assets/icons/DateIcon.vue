<script setup>
const props = defineProps({
    type : {
        type: String,
        default: 'creation'
    }
})
</script>

<template>
  <svg v-if="type==='creation'" class="date-icon" :aria-label="$t('DateIcon.CreationDate')"
       viewBox="0 0 26 24" fill="none" xmlns="http://www.w3.org/2000/svg">
    <path d="M1 8.5V22.5H24.5V8.5M1 8.5H24.5M1 8.5V3.5H7.5M24.5 8.5V3.5H18.5M7.5 3.5H18.5M7.5 3.5V0M7.5 3.5V6M18.5 3.5V0M18.5 3.5V6" stroke="#262A31" stroke-width="2"/>
    <title>{{ $t('DateIcon.created') }}</title>
  </svg>

  <svg v-else-if="type==='lastEdited'" class="date-icon" :aria-label="$t('DateIcon.lastEditedDate')"
       viewBox="0 0 26 24" fill="none" xmlns="http://www.w3.org/2000/svg">
    <path d="M1.5 8.5V22.5H25V8.5M1.5 8.5H25M1.5 8.5V3.5H8M25 8.5V3.5H19M8 3.5H19M8 3.5V0M8 3.5V6M19 3.5V0M19 3.5V6" stroke="#262A31" stroke-width="2"/>
    <path d="M6.5 12.5H11.9516M19.5 12.5H17.4032M19.5 15.5H9.85484M9.01613 15.5H6.5M6.5 18.5H11.9516M12.7903 18.5H14.0484M19.5 18.5H14.8871M12.7903 12.5H16.5645" stroke="#EDAE49" stroke-width="2"/>
    <title>{{ $t('DateIcon.lastEdited') }}</title>
  </svg>

  <svg v-else-if="type==='decision'" class="date-icon" :aria-label="$t('DateIcon.decisionDate')"
       viewBox="0 0 26 24" fill="none" xmlns="http://www.w3.org/2000/svg">
    <path d="M1.5 8.5V22.5H25V8.5M1.5 8.5H25M1.5 8.5V3.5H8M25 8.5V3.5H19M8 3.5H19M8 3.5V0M8 3.5V6M19 3.5V0M19 3.5V6" stroke="#262A31" stroke-width="2"/>
    <path d="M9 15L11.5 17.5L16.5 12.5" stroke="#659B5E" stroke-width="2"/>
    <title>{{ $t('DateIcon.decision') }}</title>
  </svg>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;
.date-icon {
    width: rem(26px);
    height: rem(24px);
}
</style>