<script setup>
/*
unsaved icon
 */

const props = defineProps({
  /* controls is displayed */
  display: {
    required: true,
    type: Boolean
  }
})
</script>


<template>
  <div class="unsaved-container" :class="{'display': display}">
    <svg width="6" height="17" viewBox="0 0 6 17" fill="none" xmlns="http://www.w3.org/2000/svg">
      <path d="M0.584 0.599999H5.27L4.478 10.72H1.376L0.584 0.599999ZM0.584 14.504C0.584 13.8733 0.804 13.3673 1.244 12.986C1.684 12.59 2.24133 12.392 2.916 12.392C3.60533 12.392 4.16267 12.59 4.588 12.986C5.028 13.3673 5.248 13.8733 5.248 14.504C5.248 15.12 5.028 15.6187 4.588 16C4.16267 16.396 3.60533 16.594 2.916 16.594C2.24133 16.594 1.684 16.396 1.244 16C0.804 15.6187 0.584 15.12 0.584 14.504Z" fill="white"/>
    </svg>
  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;


.unsaved-container {
  background-color: $red;
  width: 3rem;
  height: 3rem;
  transition: 0.1s ease-in-out;


  display: none;
  justify-content: center;
  align-items: center;
}

.display {
  display: flex;
}
</style>

    