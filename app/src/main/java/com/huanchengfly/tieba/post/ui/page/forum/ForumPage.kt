package com.huanchengfly.tieba.post.ui.page.forum

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.VerticalAlignTop
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.huanchengfly.tieba.post.R
import com.huanchengfly.tieba.post.api.models.protos.frsPage.ForumInfo
import com.huanchengfly.tieba.post.arch.ImmutableHolder
import com.huanchengfly.tieba.post.arch.collectPartialAsState
import com.huanchengfly.tieba.post.arch.emitGlobalEvent
import com.huanchengfly.tieba.post.arch.emitGlobalEventSuspend
import com.huanchengfly.tieba.post.arch.onEvent
import com.huanchengfly.tieba.post.arch.pageViewModel
import com.huanchengfly.tieba.post.dataStore
import com.huanchengfly.tieba.post.ext.toastShort
import com.huanchengfly.tieba.post.getInt
import com.huanchengfly.tieba.post.models.ForumHistoryExtra
import com.huanchengfly.tieba.post.models.database.History
import com.huanchengfly.tieba.post.ui.common.theme.compose.ExtendedTheme
import com.huanchengfly.tieba.post.ui.page.LocalNavigator
import com.huanchengfly.tieba.post.ui.page.ProvideNavigator
import com.huanchengfly.tieba.post.ui.page.destinations.ForumDetailPageDestination
import com.huanchengfly.tieba.post.ui.page.destinations.ForumSearchPostPageDestination
import com.huanchengfly.tieba.post.ui.page.forum.threadlist.ForumThreadListPage
import com.huanchengfly.tieba.post.ui.page.forum.threadlist.ForumThreadListUiEvent
import com.huanchengfly.tieba.post.ui.widgets.compose.Avatar
import com.huanchengfly.tieba.post.ui.widgets.compose.AvatarPlaceholder
import com.huanchengfly.tieba.post.ui.widgets.compose.BackNavigationIcon
import com.huanchengfly.tieba.post.ui.widgets.compose.ClickMenu
import com.huanchengfly.tieba.post.ui.widgets.compose.ConfirmDialog
import com.huanchengfly.tieba.post.ui.widgets.compose.DefaultButton
import com.huanchengfly.tieba.post.ui.widgets.compose.FeedCardPlaceholder
import com.huanchengfly.tieba.post.ui.widgets.compose.LazyLoad
import com.huanchengfly.tieba.post.ui.widgets.compose.MenuScope
import com.huanchengfly.tieba.post.ui.widgets.compose.MyScaffold
import com.huanchengfly.tieba.post.ui.widgets.compose.PagerTabIndicator
import com.huanchengfly.tieba.post.ui.widgets.compose.PullToRefreshLayout
import com.huanchengfly.tieba.post.ui.widgets.compose.ScrollableTabRow
import com.huanchengfly.tieba.post.ui.widgets.compose.Sizes
import com.huanchengfly.tieba.post.ui.widgets.compose.TabClickMenu
import com.huanchengfly.tieba.post.ui.widgets.compose.Toolbar
import com.huanchengfly.tieba.post.ui.widgets.compose.picker.ListSinglePicker
import com.huanchengfly.tieba.post.ui.widgets.compose.rememberDialogState
import com.huanchengfly.tieba.post.ui.widgets.compose.rememberMenuState
import com.huanchengfly.tieba.post.ui.widgets.compose.states.StateScreen
import com.huanchengfly.tieba.post.utils.AccountUtil.LocalAccount
import com.huanchengfly.tieba.post.utils.HistoryUtil
import com.huanchengfly.tieba.post.utils.TiebaUtil
import com.huanchengfly.tieba.post.utils.appPreferences
import com.huanchengfly.tieba.post.utils.requestPinShortcut
import com.ramcosta.composedestinations.annotation.DeepLink
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.github.fornewid.placeholder.foundation.PlaceholderHighlight
import io.github.fornewid.placeholder.material3.fade
import io.github.fornewid.placeholder.material3.placeholder
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

fun getSortType(
    context: Context,
    forumName: String,
): Int {
    val defaultSortType = context.appPreferences.defaultSortType?.toIntOrNull() ?: 0
    return context.dataStore.getInt("${forumName}_sort_type", defaultSortType)
}

suspend fun setSortType(
    context: Context,
    forumName: String,
    sortType: Int,
) {
    context.dataStore.edit {
        it[intPreferencesKey("${forumName}_sort_type")] = sortType
    }
}

@Composable
private fun ForumHeaderPlaceholder(
    forumName: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AvatarPlaceholder(size = Sizes.Large)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.title_forum, forumName),
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (LocalAccount.current != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100))
                        .placeholder(
                            visible = true,
                            highlight = PlaceholderHighlight.fade(),
                        )
                        .padding(horizontal = 18.dp, vertical = 6.dp)
                ) {
                    Text(text = stringResource(id = R.string.button_sign_in), fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
private fun ForumHeader(
    forumInfoImmutableHolder: ImmutableHolder<ForumInfo>,
    onOpenForumInfo: () -> Unit,
    onBtnClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val (forum) = forumInfoImmutableHolder
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar(
                data = forum.avatar,
                size = Sizes.Large,
                contentDescription = null
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onOpenForumInfo
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.title_forum, forum.name),
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
//                    Icon(
//                        imageVector = Icons.Rounded.KeyboardArrowRight,
//                        contentDescription = null,
//                        modifier = Modifier.size(16.dp)
//                    )
                }
                AnimatedVisibility(visible = forum.is_like == 1) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        LinearProgressIndicator(
                            progress = {
                                max(
                                    0F,
                                    min(
                                        1F,
                                        forum.cur_score * 1.0F / (max(
                                            1.0F,
                                            forum.levelup_score * 1.0F
                                        ))
                                    )
                                )
                            },
                            modifier = Modifier
                                .clip(RoundedCornerShape(100))
                                .height(8.dp),
                            color = ExtendedTheme.colorScheme.primary,
                            trackColor = ExtendedTheme.colorScheme.primary.copy(alpha = 0.25f),
                        )
                        Text(
                            text = stringResource(
                                id = R.string.tip_forum_header_liked,
                                forum.user_level.toString(),
                                forum.level_name
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = ExtendedTheme.colorScheme.textSecondary,
                            fontSize = 10.sp,
                        )
                    }
                }
            }
            val btnEnabled =
                (forum.is_like != 1) || (forum.sign_in_info?.user_info?.is_sign_in != 1)
            if (LocalAccount.current != null) {
                DefaultButton(
                    onClick = onBtnClick,
                    elevation = null,
                    shape = RoundedCornerShape(100),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ExtendedTheme.colorScheme.primary,
                        contentColor = ExtendedTheme.colorScheme.onAccent
                    ),
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 6.dp),
                    enabled = btnEnabled
                ) {
                    val text = when {
                        forum.is_like != 1 -> stringResource(id = R.string.button_follow)
                        forum.sign_in_info?.user_info?.is_sign_in == 1 -> stringResource(
                            id = R.string.button_signed_in,
                            forum.sign_in_info.user_info.cont_sign_num
                        )

                        else -> stringResource(id = R.string.button_sign_in)
                    }
                    Text(text = text, fontSize = 13.sp)
                }
            }
        }
//        Row(
//            modifier = Modifier
//                .clip(RoundedCornerShape(8.dp))
//                .background(color = ExtendedTheme.colors.chip)
//                .padding(vertical = 20.dp),
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            StatCardItem(
//                statNum = forum.member_num,
//                statText = stringResource(id = R.string.text_stat_follow)
//            )
//            HorizontalDivider(color = Color(if (ExtendedTheme.colors.isNightMode) 0xFF808080 else 0xFFDEDEDE))
//            StatCardItem(
//                statNum = forum.thread_num,
//                statText = stringResource(id = R.string.text_stat_threads)
//            )
//            HorizontalDivider(color = Color(if (ExtendedTheme.colors.isNightMode) 0xFF808080 else 0xFFDEDEDE))
//            StatCardItem(
//                statNum = forum.post_num,
//                statText = stringResource(id = R.string.title_stat_posts_num)
//            )
//        }
    }
}

private fun shareForum(context: Context, forumName: String) {
    TiebaUtil.shareText(
        context,
        "https://tieba.baidu.com/f?kw=$forumName",
        context.getString(R.string.title_forum, forumName)
    )
}

private suspend fun sendToDesktop(
    context: Context,
    forum: ForumInfo,
    onSuccess: () -> Unit = {},
    onFailure: (failureMessage: String) -> Unit = {}
) {
    requestPinShortcut(
        context,
        "forum_${forum.id}",
        forum.avatar,
        context.getString(R.string.title_forum, forum.name),
        Intent(Intent.ACTION_VIEW).setData(Uri.parse("tblite://forum/${forum.name}")),
        onSuccess = onSuccess,
        onFailure = onFailure
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Destination(
    deepLinks = [
        DeepLink(uriPattern = "tblite://forum/{forumName}")
    ]
)
@Composable
fun ForumPage(
    forumName: String,
    viewModel: ForumViewModel = pageViewModel(),
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

    LazyLoad(loaded = viewModel.initialized) {
        viewModel.send(ForumUiIntent.Load(forumName, getSortType(context, forumName)))
        viewModel.initialized = true
    }

    val snackbarHostState = remember { SnackbarHostState() }
    viewModel.onEvent<ForumUiEvent.SignIn.Success> {
        snackbarHostState.showSnackbar(
            message = context.getString(
                R.string.toast_sign_success,
                "${it.signBonusPoint}",
                "${it.userSignRank}"
            )
        )
    }
    viewModel.onEvent<ForumUiEvent.SignIn.Failure> {
        snackbarHostState.showSnackbar(
            message = context.getString(R.string.toast_sign_failed, it.errorMsg)
        )
    }
    viewModel.onEvent<ForumUiEvent.Like.Success> {
        snackbarHostState.showSnackbar(
            message = context.getString(R.string.toast_like_success, it.memberSum)
        )
    }
    viewModel.onEvent<ForumUiEvent.Like.Failure> {
        snackbarHostState.showSnackbar(
            message = context.getString(R.string.toast_like_failed, it.errorMsg)
        )
    }
    viewModel.onEvent<ForumUiEvent.Unlike.Success> {
        snackbarHostState.showSnackbar(
            message = context.getString(R.string.toast_unlike_success)
        )
    }
    viewModel.onEvent<ForumUiEvent.Unlike.Failure> {
        snackbarHostState.showSnackbar(
            message = context.getString(R.string.toast_unlike_failed, it.errorMsg)
        )
    }

    val isLoading by viewModel.uiState.collectPartialAsState(
        prop1 = ForumUiState::isLoading,
        initial = false
    )
    val isError by viewModel.uiState.collectPartialAsState(
        prop1 = ForumUiState::isError,
        initial = false
    )
    val forumInfo by viewModel.uiState.collectPartialAsState(
        prop1 = ForumUiState::forum,
        initial = null
    )
    val tbs by viewModel.uiState.collectPartialAsState(prop1 = ForumUiState::tbs, initial = null)

    val account = LocalAccount.current
    val pagerState = rememberPagerState { 2 }

    val currentPage by remember {
        derivedStateOf {
            pagerState.currentPage
        }
    }

    var heightOffset by rememberSaveable { mutableFloatStateOf(0f) }
    var headerHeight by rememberSaveable {
        mutableFloatStateOf(
            with(density) {
                (Sizes.Large + 16.dp * 2).toPx()
            }
        )
    }

    val isShowTopBarArea by remember {
        derivedStateOf {
            heightOffset.absoluteValue < headerHeight
        }
    }

    val unlikeDialogState = rememberDialogState()

    LaunchedEffect(forumInfo) {
        if (forumInfo != null) {
            val (forum) = forumInfo as ImmutableHolder<ForumInfo>
            HistoryUtil.saveHistory(
                History(
                    title = context.getString(R.string.title_forum, forum.name),
                    timestamp = System.currentTimeMillis(),
                    avatar = forum.avatar,
                    type = HistoryUtil.TYPE_FORUM,
                    data = forum.name,
                    extras = Json.encodeToString(ForumHistoryExtra(forum.id))
                ),
                true
            )
        }
    }

    if (account != null && forumInfo != null) {
        ConfirmDialog(
            dialogState = unlikeDialogState,
            onConfirm = {
                viewModel.send(
                    ForumUiIntent.Unlike(forumInfo!!.get { id }, forumName, tbs ?: account.tbs)
                )
            },
            title = {
                Text(
                    text = stringResource(id = R.string.title_dialog_unfollow_forum, forumName)
                )
            }
        )
    }

    ProvideNavigator(navigator = navigator) {
        StateScreen(
            modifier = Modifier.fillMaxSize(),
            isEmpty = forumInfo == null,
            isError = isError,
            isLoading = isLoading,
            onReload = {
                viewModel.send(ForumUiIntent.Load(forumName, getSortType(context, forumName)))
            },
            loadingScreen = {
                LoadingPlaceholder(forumName)
            }
        ) {
            MyScaffold(
                snackbarHostState = snackbarHostState,
                containerColor = Color.Transparent,
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    ForumToolbar(
                        forumName = forumName,
                        showTitle = !isShowTopBarArea,
                        menuContent = {
                            DropdownMenuItem(
                                text = {
                                    Text(text = stringResource(id = R.string.title_share))
                                },
                                onClick = {
                                    shareForum(context, forumName)
                                    dismiss()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = stringResource(id = R.string.title_send_to_desktop)) },
                                onClick = {
                                    if (forumInfo != null) {
                                        val (forum) = forumInfo!!
                                        coroutineScope.launch {
                                            sendToDesktop(
                                                context,
                                                forum,
                                                onSuccess = {
                                                    coroutineScope.launch {
                                                        snackbarHostState.showSnackbar(
                                                            message = context.getString(R.string.toast_send_to_desktop_success)
                                                        )
                                                    }
                                                },
                                                onFailure = { failureMessage ->
                                                    coroutineScope.launch {
                                                        snackbarHostState.showSnackbar(
                                                            message = context.getString(
                                                                R.string.toast_send_to_desktop_failed,
                                                                failureMessage
                                                            )
                                                        )
                                                    }
                                                }
                                            )
                                        }
                                    }
                                    dismiss()
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(text = stringResource(id = R.string.title_unfollow))
                                },
                                onClick = {
                                    unlikeDialogState.show()
                                    dismiss()
                                }
                            )
                        },
                        forumId = forumInfo?.get { id }
                    )
                },
                floatingActionButton = {
                    if (context.appPreferences.forumFabFunction != "hide") {
                        FloatingActionButton(
                            onClick = {
                                when (context.appPreferences.forumFabFunction) {
                                    "refresh" -> {
                                        coroutineScope.launch {
                                            emitGlobalEventSuspend(
                                                ForumThreadListUiEvent.BackToTop(
                                                    currentPage == 1
                                                )
                                            )
                                            emitGlobalEventSuspend(
                                                ForumThreadListUiEvent.Refresh(
                                                    currentPage == 1,
                                                    getSortType(
                                                        context,
                                                        forumName
                                                    )
                                                )
                                            )
                                        }
                                    }

                                    "back_to_top" -> {
                                        coroutineScope.launch {
                                            emitGlobalEvent(
                                                ForumThreadListUiEvent.BackToTop(
                                                    currentPage == 1
                                                )
                                            )
                                        }
                                    }

                                    else -> {
                                        context.toastShort(R.string.toast_feature_unavailable)
                                    }
                                }
                            },
                            containerColor = ExtendedTheme.colorScheme.windowBackground,
                            contentColor = ExtendedTheme.colorScheme.primary,
                            modifier = Modifier.navigationBarsPadding()
                        ) {
                            Icon(
                                imageVector = when (context.appPreferences.forumFabFunction) {
                                    "refresh" -> Icons.Rounded.Refresh
                                    "back_to_top" -> Icons.Rounded.VerticalAlignTop
                                    else -> Icons.Rounded.Add
                                },
                                contentDescription = null
                            )
                        }
                    }
                }
            ) { contentPadding ->
                var isFakeLoading by remember { mutableStateOf(false) }

                LaunchedEffect(isFakeLoading) {
                    if (isFakeLoading) {
                        delay(1000)
                        isFakeLoading = false
                    }
                }

                PullToRefreshLayout(
                    refreshing = isFakeLoading,
                    onRefresh = {
                        coroutineScope.emitGlobalEvent(
                            ForumThreadListUiEvent.Refresh(
                                currentPage == 1,
                                getSortType(context, forumName)
                            )
                        )
                        isFakeLoading = true
                    }
                ) {
                    val headerNestedScrollConnection = remember {
                        object : NestedScrollConnection {
                            override fun onPreScroll(
                                available: Offset,
                                source: NestedScrollSource,
                            ): Offset {
                                if (available.y < 0) {
                                    val prevHeightOffset = heightOffset
                                    heightOffset = max(heightOffset + available.y, -headerHeight)
                                    if (prevHeightOffset != heightOffset) {
                                        return available.copy(x = 0f)
                                    }
                                }

                                return Offset.Zero
                            }

                            override fun onPostScroll(
                                consumed: Offset,
                                available: Offset,
                                source: NestedScrollSource,
                            ): Offset {
                                if (available.y > 0f) {
                                    // Adjust the height offset in case the consumed delta Y is less than what was
                                    // recorded as available delta Y in the pre-scroll.
                                    val prevHeightOffset = heightOffset
                                    heightOffset = min(heightOffset + available.y, 0f)
                                    if (prevHeightOffset != heightOffset) {
                                        return available.copy(x = 0f)
                                    }
                                }

                                return Offset.Zero
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .padding(contentPadding)
                            .nestedScroll(headerNestedScrollConnection)
                    ) {
                        Column {
                            val containerHeight by remember {
                                derivedStateOf {
                                    with(density) {
                                        (headerHeight + heightOffset).toDp()
                                    }
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .height(containerHeight)
                                    .clipToBounds()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .wrapContentHeight(
                                            align = Alignment.Bottom,
                                            unbounded = true
                                        )
                                        .onSizeChanged {
                                            headerHeight = it.height.toFloat()
                                        }
                                ) {
                                    forumInfo?.let { holder ->
                                        ForumHeader(
                                            forumInfoImmutableHolder = holder,
                                            onOpenForumInfo = {
                                                navigator.navigate(
                                                    ForumDetailPageDestination(
                                                        forumId = holder.get { this.id })
                                                )
                                            },
                                            onBtnClick = {
                                                val (forum) = holder
                                                when {
                                                    forum.is_like != 1 -> viewModel.send(
                                                        ForumUiIntent.Like(
                                                            forum.id,
                                                            forum.name,
                                                            tbs ?: account!!.tbs
                                                        )
                                                    )

                                                    forum.sign_in_info?.user_info?.is_sign_in != 1 -> {
                                                        viewModel.send(
                                                            ForumUiIntent.SignIn(
                                                                forum.id,
                                                                forum.name,
                                                                tbs ?: account!!.tbs
                                                            )
                                                        )
                                                    }
                                                }
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp)
                                        )
                                    }
                                }
                            }

                            val tabTextStyle = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                letterSpacing = 0.sp
                            )

                            ScrollableTabRow(
                                selectedTabIndex = currentPage,
                                indicator = { tabPositions ->
                                    PagerTabIndicator(
                                        pagerState = pagerState,
                                        tabPositions = tabPositions
                                    )
                                },
                                divider = {},
                                backgroundColor = Color.Transparent,
                                contentColor = ExtendedTheme.colorScheme.primary,
                                edgePadding = 0.dp,
                                modifier = Modifier
                                    .wrapContentWidth(align = Alignment.Start)
                                    .align(Alignment.Start)
                            ) {
                                var currentSortType by remember {
                                    mutableIntStateOf(
                                        getSortType(
                                            context,
                                            forumName
                                        )
                                    )
                                }
                                TabClickMenu(
                                    selected = currentPage == 0,
                                    onClick = {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(0)
                                        }
                                    },
                                    text = {
                                        Text(
                                            text = stringResource(id = R.string.tab_forum_latest),
                                            style = tabTextStyle
                                        )
                                    },
                                    menuContent = {
                                        ListSinglePicker(
                                            itemTitles = persistentListOf(
                                                stringResource(id = R.string.title_sort_by_reply),
                                                stringResource(id = R.string.title_sort_by_send)
                                            ),
                                            itemValues = persistentListOf(0, 1),
                                            selectedPosition = currentSortType,
                                            onItemSelected = { _, _, value, changed ->
                                                if (changed) {
                                                    currentSortType = value
                                                    coroutineScope.launch {
                                                        setSortType(context, forumName, value)
                                                        emitGlobalEvent(
                                                            ForumThreadListUiEvent.Refresh(
                                                                currentPage == 1,
                                                                value
                                                            )
                                                        )
                                                    }
                                                }
                                                dismiss()
                                            }
                                        )
                                    },
                                    selectedContentColor = ExtendedTheme.colorScheme.primary,
                                    unselectedContentColor = ExtendedTheme.colorScheme.textSecondary
                                )
                                Tab(
                                    selected = currentPage == 1,
                                    onClick = {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(1)
                                        }
                                    },
                                    selectedContentColor = ExtendedTheme.colorScheme.primary,
                                    unselectedContentColor = ExtendedTheme.colorScheme.textSecondary
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .height(48.dp)
                                            .padding(horizontal = 16.dp)
                                    ) {
                                        Text(
                                            text = stringResource(id = R.string.tab_forum_good),
                                            style = tabTextStyle
                                        )
                                    }
                                }
                            }

                            if (forumInfo != null) {
                                HorizontalPager(
                                    state = pagerState,
                                    modifier = Modifier.fillMaxSize(),
                                    key = { it },
                                    verticalAlignment = Alignment.Top,
                                    userScrollEnabled = true,
                                ) {
                                    ForumThreadListPage(
                                        forumId = forumInfo!!.get { id },
                                        forumName = forumInfo!!.get { name },
                                        isGood = it == 1,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadingPlaceholder(
    forumName: String
) {
    val context = LocalContext.current

    MyScaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            ForumToolbar(
                forumName = forumName,
                showTitle = false,
                menuContent = {
                    DropdownMenuItem(
                        text = {
                            Text(text = stringResource(id = R.string.title_share))
                        },
                        onClick = {
                            shareForum(context, forumName)
                            dismiss()
                        },
                    )
                }
            )
        },
        containerColor = Color.Transparent
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            ForumHeaderPlaceholder(
                forumName = forumName,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            )
            Row(modifier = Modifier.height(48.dp)) {
                persistentListOf(
                    stringResource(id = R.string.tab_forum_latest),
                    stringResource(id = R.string.tab_forum_good),
                ).fastForEach {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = it,
                            modifier = Modifier.placeholder(
                                visible = true,
                                highlight = PlaceholderHighlight.fade(),
                            ),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.sp,
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                }
            }
            repeat(4) {
                FeedCardPlaceholder()
            }
        }
    }
}

@Composable
private fun ForumToolbar(
    forumName: String,
    showTitle: Boolean,
    menuContent: @Composable (MenuScope.() -> Unit)? = null,
    forumId: Long? = null,
) {
    val navigator = LocalNavigator.current
    Toolbar(
        title = {
            if (showTitle) Text(
                text = stringResource(id = R.string.title_forum, forumName)
            )
        },
        navigationIcon = { BackNavigationIcon(onBackPressed = { navigator.navigateUp() }) },
        actions = {
            if (forumId != null) {
                IconButton(
                    onClick = {
                        navigator.navigate(ForumSearchPostPageDestination(forumName, forumId))
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = stringResource(id = R.string.btn_search_in_forum)
                    )
                }
            }
            Box {
                if (menuContent != null) {
                    val menuState = rememberMenuState()
                    ClickMenu(
                        menuContent = menuContent,
                        menuState = menuState,
                        triggerShape = CircleShape
                    ) {
                        Box(
                            modifier = Modifier.size(48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.MoreVert,
                                contentDescription = stringResource(id = R.string.btn_more)
                            )
                        }
                    }
                }
            }
        }
    )
}

