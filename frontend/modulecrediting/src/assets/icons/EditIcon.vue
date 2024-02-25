<template>
    <div class="edit-container">
        <svg width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
<path d="M8.5 4H1V19H16V11.5M7.5 12.5L8 10L16 2L18 4L10 12L7.5 12.5Z" stroke="#262A31" stroke-width="2"/>
</svg>

    </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.edit-container {
  @include smallHighlightBox();
  transition: 0.1s ease-in-out;

  &:hover, &:focus {
    background-color: $gray-hover;
    cursor: pointer;
  }

  &:focus-visible {
    outline: 2px solid $dark-gray-hover;
    outline-offset: 2px;
  }
}
</style>