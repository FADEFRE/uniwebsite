<script setup>
import { useRouter } from 'vue-router'
import { ref } from 'vue'
import { url } from "@/scripts/url-config";
import axios from "axios";
import HomepageContainer from '../components/HomepageContainer.vue';
import SideInfoContainer from '../components/SideInfoContainer.vue';
import ButtonLink from '../components/ButtonLink.vue';

const router = useRouter()

const isInvalid = ref(false)
const id = ref('')

const openDetailView = () => {
    const formattedId = getFormattedId();
    axios.get(url + `/api/applications/${formattedId}/exists`)
        .then(response => {
            if (response.data) {
                const routeData = router.resolve({ name: 'statusDetail', params: { id: formattedId } });
                window.open(routeData.href, '_top');
            } else {
                isInvalid.value = true
            }
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
            <HomepageContainer :header="$t('make application')"
                :text="$t('create an application to have modules from other universities recognized at the University of Leipzig.')">
                <ButtonLink @click="goToSubmitApplication">{{$t('make application')}}</ButtonLink>
            </HomepageContainer>

            <!-- HomepageContainer StatusView -->
            <HomepageContainer :header="$t('view status')"
                :text="$t('view the status of applications that have already been submitted using the 6-digit process number.')">
                <InputText v-model="id" :class="{ 'p-invalid': isInvalid }" placeholder="0-0-0-0-0-0" @keydown.enter.prevent="openDetailView" @input="validateInput" />
                <ButtonLink @click="openDetailView">{{$t('view status')}}</ButtonLink>
            </HomepageContainer>
        </div>

        <div class="side-infos-container">
            <!--SideInfoContainerfür Antragprozess -->
            <SideInfoContainer :heading="$t('application process')">
                <ul class="list-container">
                    <li class="list-item">{{$t('submit an application online')}}</li>
                    <li class="list-item">{{$t('view status online using process number')}}</li>
                    <li class="list-item">{{$t('wait for the PAV`s decision')}}</li>
                    <li class="list-item">{{$t('go to the study office with the completed application')}}</li>
                </ul>
            </SideInfoContainer>
        </div>

    </div>
</template>


<style scoped lang="scss">
@import '../assets/variables.scss';
@import '../assets/mixins.scss';

.main {
    @include main();
}

.homepage-content {
    @include verticalList(big);
    width: 100%;
}

.side-infos-container {
    @include verticalList(big);
    
    @media only screen and (max-width: 1000px) {
    width: 100%;
  }
}
</style>
