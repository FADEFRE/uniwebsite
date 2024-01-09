<!--
button with link icon
props:
- route name to be opened on click
slots:
  - default slot for button name
-->

<script setup>
import { useRouter } from "vue-router";

const props = defineProps({
  routeName: {
    required: true,
    type: String
  }
})

const router = useRouter()

const openLink = () => {
  router.push({name: props.routeName})
}
</script>

<template>
  <div >
    <Button @click="openLink" class="button-container">
      <slot></slot>
      <img src="../assets/icons/ArrowWhite.svg" class="arrow-icon">
    </Button>
  </div>
</template>

<style scoped lang="scss">
@import '../assets/variables.scss';
.button-container{
  display: flex;
  gap: 0.625rem;

  &:hover{
    background-color: $red;
  }
}
.arrow-icon{
  transform: rotate(-90deg);
} 
</style>