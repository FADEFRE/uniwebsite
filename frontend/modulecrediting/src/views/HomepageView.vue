<script setup>
import { useRouter } from 'vue-router'
import { ref } from 'vue'
import { getApplicationExists } from "@/scripts/axios-requests";
import HomepageContainer from '../components/HomepageContainer.vue';
import SideInfoContainer from '../components/SideInfoContainer.vue';
import ButtonLink from '../components/ButtonLink.vue';

const router = useRouter()

const isInvalid = ref(false)
const id = ref('')

const openDetailView = () => {
    const formattedId = getFormattedId();
    console.log(id.value);
    console.log(formattedId);
    getApplicationExists(formattedId)
        .then(data => {
            if (data === false) {
                isInvalid.value = true;
                return;
            }

            const routeData = router.resolve({ name: 'statusDetail', params: { id: formattedId } });
            window.open(routeData.href, '_top');
        })
}

const goToSubmitApplication = () => {
    router.push({ name: 'submitApplication' })
}

const handleEnterKey = (event) => {
    if (event.key === 'Enter') {
        openDetailView();
    }
}

const validateInput = () => {
    id.value = id.value.replace(/[^0-9]/g, '');
    id.value = id.value.replace(/(\d)(?=(\d{1,5})+$)/g, '$1-');
    id.value = id.value.slice(0, 11);
}

const getFormattedId = () => {
    return id.value.replace(/-/g, '');
}

</script>

<template>
    <div class="main">
        <div class="homepage-content">
            <!-- HomepageContainer Application -->
            <HomepageContainer :header="$t('homepage.makeApplication')"
                :text="$t('homepage.makeApplicationExplanation')">
                <ButtonLink @click="goToSubmitApplication">{{ $t('homepage.makeApplication') }}</ButtonLink>
            </HomepageContainer>

            <!-- HomepageContainer StatusView -->
            <HomepageContainer :header="$t('homepage.viewStatus')"
                :text="$t('homepage.viewStatusExplanation')">
                <div class="input-button-container">
                    <InputText v-model="id" :class="{ 'invalid': isInvalid }" class="status-input"
                        placeholder="0-0-0-0-0-0" @keydown.enter.prevent="openDetailView" @input="validateInput" />
                    <ButtonLink @click="openDetailView">{{ $t('homepage.viewStatus') }}</ButtonLink>
                </div>
                <small v-if="isInvalid" class="invalid-text">Punkte müssen als Zahl angegeben werden</small>
            </HomepageContainer>
        </div>

        <div class="side-infos-container">
            <!--SideInfoContainerfür Antragprozess -->
            <SideInfoContainer :heading="$t('homepage.sideInfo.applicationProcess')">
                <ul class="list-container">
                    <li class="list-item">{{ $t('homepage.sideInfo.submitApplication') }}</li>
                    <li class="list-item">{{ $t('homepage.sideInfo.viewStatus') }}</li>
                    <li class="list-item">{{ $t('homepage.sideInfo.wait') }}</li>
                    <li class="list-item">{{ $t('homepage.sideInfo.goToStudy') }}</li>
                </ul>
            </SideInfoContainer>
        </div>

    </div>
</template>


<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;

.main {
    @include main();
}

.homepage-content {
    @include verticalList(big);
    width: 100%;
}

.side-infos-container {
    @include sideInfoListContainer();
}

.status-input {
    border: none;
    background-color: $gray;
    &:hover {
        background-color: $gray-hover;
    }
    &:focus::placeholder {
        color: transparent;
    }
}
</style>
