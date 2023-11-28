<script setup>
const emit = defineEmits(['addModuleApplication'])

const addModuleApplication = () => {emit('addModuleApplication')}
</script>

<template>
  <div class="button-container">
    <button @click="addModuleApplication" class="button-element">Weiteres Modul hinzufügen ...</button>
  </div>
</template>

<style scoped>
.button-container {
  margin: 10px;
}

.button-element {
  height: 3rem;
  width: 100%;

  background-color: #fcfcfc;
  border-width: 1px;
  border-radius: 3px;
  border-color: #e0e0e0;
  border-style: solid;

  text-align: left;
  text-indent: 10px;
  font-size: 20px;
  font-weight: bold;

  transition: background-color 0.1s, border-color 0.1s;
}

.button-element:hover {
  background-color: #f4f4f4;
  border-color: #c8c8c8;
}
</style>