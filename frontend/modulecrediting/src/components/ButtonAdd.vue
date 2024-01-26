<!--
button with add icon
slots:
  - default slot for button name
-->

<script setup>

</script>

<template>
    <Button class="button-container">
      <slot></slot>
      <img src="../assets/icons/PlusIcon.svg">
    </Button>
</template>

<style scoped lang="scss">
@import '../assets/variables.scss';
.button-container{
  display: flex;
  gap: 0.625rem;

}


</style>