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
  <div>
    <Button @click="openLink">
      <slot></slot>
      <img src="../assets/icons/ArrowWhite.svg">
    </Button>
  </div>
</template>

<style scoped>

</style>