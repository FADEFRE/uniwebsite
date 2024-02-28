<script setup>
const props = defineProps({
  type: {
    type: String,
    required: true
  }
})
</script>

<template>
  <div class="controll-container">


    <svg v-if="type === 'expand'" width="18" height="36" viewBox="0 0 18 36" fill="none" xmlns="http://www.w3.org/2000/svg">
      <g clip-path="url(#clip0_853_9031)">
        <path fill-rule="evenodd" clip-rule="evenodd"
          d="M0.599854 8.90396L3.12349 11.3999L8.99985 5.59077L14.8762 11.3999L17.3999 8.90396L8.99985 0.599903L0.599854 8.90396Z"
          fill="white" />
      </g>
      <g clip-path="url(#clip1_853_9031)">
        <path fill-rule="evenodd" clip-rule="evenodd"
          d="M17.4001 27.096L14.8765 24.6001L9.00015 30.4092L3.12378 24.6001L0.600147 27.096L9.00015 35.4001L17.4001 27.096Z"
          fill="white" />
      </g>
      <defs>
        <clipPath id="clip0_853_9031">
          <rect width="18" height="12" fill="white" transform="matrix(-1 0 0 -1 18 12)" />
        </clipPath>
        <clipPath id="clip1_853_9031">
          <rect width="18" height="12" fill="white" transform="translate(0 24)" />
        </clipPath>
      </defs>
    </svg>

    <svg v-else width="18" height="36" viewBox="0 0 18 36" fill="none" xmlns="http://www.w3.org/2000/svg">
      <g clip-path="url(#clip0_853_8967)">
        <path fill-rule="evenodd" clip-rule="evenodd"
          d="M17.4001 3.09604L14.8765 0.600098L9.00015 6.40923L3.12378 0.600098L0.600147 3.09604L9.00015 11.4001L17.4001 3.09604Z"
          fill="white" />
      </g>
      <g clip-path="url(#clip1_853_8967)">
        <path fill-rule="evenodd" clip-rule="evenodd"
          d="M0.599854 32.904L3.12349 35.3999L8.99985 29.5908L14.8762 35.3999L17.3999 32.904L8.99985 24.5999L0.599854 32.904Z"
          fill="white" />
      </g>
      <defs>
        <clipPath id="clip0_853_8967">
          <rect width="18" height="12" fill="white" />
        </clipPath>
        <clipPath id="clip1_853_8967">
          <rect width="18" height="12" fill="white" transform="matrix(-1 0 0 -1 18 36)" />
        </clipPath>
      </defs>
    </svg>
  </div>
</template>

<style lang="scss" scoped>
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.controll-container {
  width: 3rem;
  height: 4rem;
  background-color: $dark-gray;
  transition: 0.1s ease-in-out;

  &:hover,
  &:focus {
    background-color: $dark-gray-hover;
    cursor: pointer;
  }

  &:focus {
    outline: 2.5px solid $dark-gray-hover;
    outline-offset: 2px;
  }

  display: flex;
  justify-content: center;
  align-items: center;

  @include breakpoint(m) {
    display: none;
  }
}</style>