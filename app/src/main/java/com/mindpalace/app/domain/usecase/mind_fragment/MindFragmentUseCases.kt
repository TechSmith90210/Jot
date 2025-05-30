package com.mindpalace.app.domain.usecase.mind_fragment

data class MindFragmentUseCases(
    val createFragment: CreateFragmentUseCase,
    val getFragmentsByLastOpened: GetFragmentsByLastOpenedUseCase,
    val getFragmentsByCreatedAt: GetFragmentsByCreatedAtUseCase,
    val getAllFragments: GetAllFragmentsUseCase,
    val getFragment: GetFragmentUseCase,
    val updateFragment: UpdateFragmentUseCase,
    val deleteFragment: DeleteFragmentUseCase
)
