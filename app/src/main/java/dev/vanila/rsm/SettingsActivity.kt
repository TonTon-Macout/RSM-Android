package dev.vanila.rsm

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.android.material.switchmaterial.SwitchMaterial
import android.widget.LinearLayout
import android.widget.Toast
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import androidx.core.graphics.toColorInt
import androidx.core.view.WindowCompat

import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.Drawable


import android.graphics.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog

import android.content.Intent
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebStorage
import android.webkit.WebView
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.core.content.ContextCompat


import com.google.android.material.color.MaterialColors
import androidx.core.net.toUri




import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import kotlin.math.roundToInt
import kotlin.text.isNotEmpty
import kotlin.text.trim


class SettingsActivity : AppCompatActivity() {



    // Переменные для элементов управления
    private lateinit var switchSnowman: SwitchMaterial
    private lateinit var switchSnowmanAnimation: SwitchMaterial
    private lateinit var switchLabel: SwitchMaterial
    private lateinit var switchOpenLastDevice: SwitchMaterial
    private lateinit var switchSolidColorIcon: SwitchMaterial
    private lateinit var switchIconInLabel: SwitchMaterial
    private lateinit var switchDoubleClickRefresh: SwitchMaterial
    private lateinit var switchEasterEgg: SwitchMaterial
    private lateinit var switchBtnBack: SwitchMaterial
    private lateinit var colorPickerButton: LinearLayout
    private lateinit var colorPreview: View
    private lateinit var backButton: LinearLayout
    private lateinit var iconInLabelLayout: LinearLayout
    private lateinit var doubleClickRefreshLayout: LinearLayout
    private lateinit var themeSpinner: Spinner // Спиннер для выбора темы


    private lateinit var colorPickerButtonBase: LinearLayout
    private lateinit var colorPickerButtonText: LinearLayout
    private lateinit var colorPickerButtonButton: LinearLayout
    private lateinit var colorPickerButtonButtonText: LinearLayout
    private lateinit var colorPickerButtonButtonStroke: LinearLayout

    private lateinit var colorPreviewBase: View
    private lateinit var colorPreviewText: View
    private lateinit var colorPreviewButton: View
    private lateinit var colorPreviewButtonText: View
    private lateinit var colorPreviewButtonStroke: View




    private lateinit var switchAddressInput: SwitchMaterial
    private lateinit var switchPingDevice: SwitchMaterial
    private lateinit var switchMarkInactive: SwitchMaterial
    private lateinit var switchVibrato: SwitchMaterial
    private lateinit var switchVibratoPing: SwitchMaterial

    private lateinit var switchBlurHeader: SwitchMaterial

    private lateinit var switchHistory: SwitchMaterial
    private lateinit var switchSwipeToRefresh: SwitchMaterial

    private lateinit var switchSwipeOpenPanel: SwitchMaterial

    private lateinit var switchCheckNewVersion: SwitchMaterial

    private lateinit var switchSplitNetwork: SwitchMaterial
    private lateinit var switchSplitNetworkManual: SwitchMaterial

    private lateinit var switchDontShowLocationDialog: SwitchMaterial
    private lateinit var dontShowLocationDialogContainer: LinearLayout





    private lateinit var networksRecyclerView: RecyclerView
    private lateinit var networkAdapter: NetworkAdapter
    private lateinit var deviceManager: DeviceManager

    private lateinit var labelInfo: ImageView
    private lateinit var checkVersionInfo: ImageView
    private lateinit var splitNetworkInfo: ImageView
    private lateinit var dontLocationInfo: ImageView
    private lateinit var splitManualInfo: ImageView
    private lateinit var networksInfo: ImageView
    private lateinit var pingInfo: ImageView
    private lateinit var historyInfo: ImageView
    private lateinit var swipeToRefreshInfo: ImageView
    private lateinit var swipeOpenPanelInfo: ImageView
    private lateinit var headerBlurInfo: ImageView

    class HeaderSeparatorDrawable(private val backgroundColor: Int,private val cornerRadius: Float) : Drawable() {

        // Краска для заливки основной фигуры
        private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = backgroundColor
            style = Paint.Style.FILL
        }


        private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 44f // Увеличим толщину линии, которую будем размывать
            maskFilter = BlurMaskFilter(10f, BlurMaskFilter.Blur.OUTER) // Оставим размытие, но можно будет его настроить
        }
        // Рассеянная, светлая тень (которую мы уже имеем)
        private val ambientShadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 28f // Широкая линия для большого размытия
            maskFilter = BlurMaskFilter(16f, BlurMaskFilter.Blur.OUTER)
        }

        // Точечная, темная тень (новая)
        private val spotShadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 18f  // Узкая линия для четкой тени
            maskFilter = BlurMaskFilter(4f, BlurMaskFilter.Blur.OUTER)
        }
        // Точечная, темная тень (новая)
        private val spotyShadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 2f  // Узкая линия для четкой тени
            maskFilter = BlurMaskFilter(2f, BlurMaskFilter.Blur.OUTER)
        }

        // Контур для основной фигуры (прямоугольник + "ушки")
        private val shapePath = Path()
        // Контур ТОЛЬКО для нижней границы, по которой пойдет тень
        private val shadowBoundaryPath = Path()

        override fun onBoundsChange(bounds: Rect) {
            super.onBoundsChange(bounds)
            val width = bounds.width().toFloat()
            val height = bounds.height().toFloat()
            val rectHeight = height - cornerRadius

            // --- 1. Контур основной фигуры (не трогаем) ---
            shapePath.reset()
            shapePath.addRect(0f, 0f, width, rectHeight, Path.Direction.CW)
            val leftTriangle = Path().apply {
                moveTo(0f, rectHeight)
                lineTo(cornerRadius, rectHeight)
                quadTo(0f, rectHeight, 0f, height)
                close()
            }
            shapePath.addPath(leftTriangle)
            val rightTriangle = Path().apply {
                moveTo(width, rectHeight)
                lineTo(width - cornerRadius, rectHeight)
                quadTo(width, rectHeight, width, height)
                close()
            }
            shapePath.addPath(rightTriangle)

            // --- 2. ПРАВИЛЬНЫЙ КОНТУР ДЛЯ ТЕНИ ---
            shadowBoundaryPath.reset()
            val verticalOffset = 21.0f
            val sideOffset = 21.0f
            shadowBoundaryPath.moveTo(0f + sideOffset, height)
            shadowBoundaryPath.quadTo(
                0f + sideOffset, rectHeight + verticalOffset,
                cornerRadius, rectHeight + verticalOffset
            )
            shadowBoundaryPath.lineTo(width - cornerRadius, rectHeight + verticalOffset)
            shadowBoundaryPath.quadTo(
                width - sideOffset, rectHeight + verticalOffset,
                width - sideOffset, height
            )


            // --- 3. НАСТРАИВАЕМ НАТУРАЛЬНЫЙ ГРАДИЕНТ ДЛЯ ТЕНИ ---
            shadowPaint.shader = LinearGradient(
                0f, rectHeight, // Начало градиента
                0f, height+10,     // Конец градиента
                Color.argb(100, 0, 0, 0), // Начальный цвет
                Color.TRANSPARENT,       // Конечный цвет
                Shader.TileMode.CLAMP
            )

            ambientShadowPaint.shader = LinearGradient(
                0f, rectHeight,
                0f, height+4,
                Color.argb(230, 0, 0, 0), // Менее интенсивный цвет
                Color.TRANSPARENT,
                Shader.TileMode.CLAMP
            )



// Градиент для темной, точечной тени прямо под краем
            spotShadowPaint.shader = LinearGradient(
                0f, rectHeight,
                0f, height-10, // Можно сделать затухание быстрее, уменьшив 'height'
                Color.argb(220, 0, 0, 0), // !!! Более темный, почти черный цвет у края
                Color.TRANSPARENT,
                Shader.TileMode.CLAMP
            )
            spotyShadowPaint.shader = LinearGradient(
                0f, rectHeight,
                0f, height, // Можно сделать затухание быстрее, уменьшив 'height'
                Color.argb(250, 0, 0, 0), // !!! Более темный, почти черный цвет у края
                Color.TRANSPARENT,
                Shader.TileMode.CLAMP
            )

        }


        override fun draw(canvas: Canvas) {
            // 3. Поверх всех теней рисуем основную фигуру
            canvas.drawPath(shapePath, backgroundPaint)
            // 2. Поверх нее рисуем маленькую, темную, точечную тень
            canvas.drawPath(shadowBoundaryPath, shadowPaint)
            // 1. Сначала рисуем большую, светлую, рассеянную тень
            //  canvas.drawPath(shadowBoundaryPath, ambientShadowPaint)

            // 2. Поверх нее рисуем маленькую, темную, точечную тень
            //canvas.drawPath(shadowBoundaryPath, spotShadowPaint)

            //  canvas.drawPath(shadowBoundaryPath, spotyShadowPaint)


        }

        override fun setAlpha(alpha: Int) {
            backgroundPaint.alpha = alpha
            shadowPaint.alpha = alpha
        }

        override fun setColorFilter(colorFilter: ColorFilter?) {
            backgroundPaint.colorFilter = colorFilter
            shadowPaint.colorFilter = colorFilter
        }

        override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deviceManager = DeviceManager(this)


        ThemeManager.applyTheme(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)


        initViews()

        loadSettings()

        val snowManContainer = findViewById<LinearLayout>(R.id.snow_man_container)
        if(switchEasterEgg.isChecked) snowManContainer.visibility = View.VISIBLE
        else                          snowManContainer.visibility = View.GONE

        val markInactiveDevicesLayout =  SettingsManager.isPingDevicesEnabled
        if(markInactiveDevicesLayout) {
            findViewById<LinearLayout>(R.id.mark_inactive_devices_layout).visibility = View.VISIBLE
            findViewById<LinearLayout>(R.id.vibro_ping_layout).visibility = View.VISIBLE
        }
        else {
            findViewById<LinearLayout>(R.id.mark_inactive_devices_layout).visibility = View.GONE
            findViewById<LinearLayout>(R.id.vibro_ping_layout).visibility = View.GONE
        }

        setupListeners()


        val separatorView = findViewById<View>(R.id.settings_separator) //
        val cornerRadiusPx = 40 * resources.displayMetrics.density

        val backgroundColor = MaterialColors.getColor(this, R.attr.SettingsBackgroundTintBottom, "#00697C".toColorInt())
        separatorView.background = HeaderSeparatorDrawable(backgroundColor, cornerRadiusPx)





    }

    private fun initViews() {
        // Инициализация переключателей
        switchSnowman = findViewById(R.id.switch_snowman)
        switchSnowmanAnimation = findViewById(R.id.switch_snowman_animation)
        switchOpenLastDevice = findViewById(R.id.switch_open_last_device)
        switchLabel = findViewById(R.id.label)
        switchSolidColorIcon = findViewById(R.id.switch_solid_color_icon)
        switchIconInLabel = findViewById(R.id.switch_icon_in_label)
        switchDoubleClickRefresh = findViewById(R.id.switch_double_click_refresh)
        switchEasterEgg = findViewById(R.id.switch_easter_egg)
        switchBtnBack = findViewById(R.id.switch_back_button_expand)
        switchAddressInput = findViewById(R.id.switch_address_input)
        switchPingDevice = findViewById(R.id.switch_ping_devices)
        switchMarkInactive = findViewById(R.id.switch_mark_inactive_devices)
        switchVibrato = findViewById(R.id.switch_vibrato)
        switchVibratoPing = findViewById(R.id.switch_vibrato_ping)
        switchBlurHeader = findViewById(R.id.switch_header)
        switchHistory = findViewById(R.id.switch_history)
        switchSwipeToRefresh = findViewById(R.id.switch_swipe_refresh)
        switchSwipeOpenPanel = findViewById(R.id.switch_swipe_open_panel)
        switchCheckNewVersion = findViewById(R.id.switch_check_version)
        switchSplitNetwork = findViewById(R.id.switch_split_network)
        switchSplitNetworkManual = findViewById(R.id.switch_split_network_manual)

        switchDontShowLocationDialog = findViewById(R.id.dont_show_location_dialog)
        dontShowLocationDialogContainer = findViewById(R.id.dont_show_location_dialog_container)

        // Инициализация кнопки выбора цвета и превью
        colorPickerButton = findViewById(R.id.color_picker_button)
        colorPreview = findViewById(R.id.color_preview)

        // Инициализация кнопки "Назад"
        backButton = findViewById(R.id.back)

        // Инициализация layout для иконки в ярлыке
        iconInLabelLayout = findViewById(R.id.icon_in_label_layout)
        doubleClickRefreshLayout = findViewById(R.id.double_click_refresh_layout)

        // Инициализация спиннера для выбора темы
        themeSpinner = findViewById(R.id.theme_spinner)

        colorPreviewBase = findViewById(R.id.color_preview_base)
        colorPreviewText = findViewById(R.id.color_preview_text)
        colorPreviewButton = findViewById(R.id.color_preview_button)
        colorPreviewButtonText = findViewById(R.id.color_preview_text_button)
        colorPreviewButtonStroke = findViewById(R.id.color_preview_button_stroke)

        colorPickerButtonBase = findViewById(R.id.color_picker_button_base)
        colorPickerButtonText = findViewById(R.id.color_picker_button_text)
        colorPickerButtonButton = findViewById(R.id.color_picker_button_button)
        colorPickerButtonButtonText = findViewById(R.id.color_picker_button_text_button)
        colorPickerButtonButtonStroke = findViewById(R.id.color_picker_button_stroke)



        labelInfo = findViewById(R.id.label_info)
        checkVersionInfo = findViewById(R.id.switch_check_version_info)
        splitNetworkInfo = findViewById(R.id.split_network_info)
        dontLocationInfo = findViewById(R.id.dont_show_location_dialog_info)
        splitManualInfo = findViewById(R.id.switch_split_network_manual_info)
        networksInfo = findViewById(R.id.networks_info)
        pingInfo = findViewById(R.id.ping_info)
        historyInfo = findViewById(R.id.history_info)
        swipeToRefreshInfo = findViewById(R.id.swipe_refresh_info)
        swipeOpenPanelInfo = findViewById(R.id.swipe_open_panel_info)
        headerBlurInfo = findViewById(R.id.header_blur_info)


    }


    private fun loadSettings() {
        switchSnowman.isChecked = SettingsManager.isSnowmanEnabled
        switchSnowmanAnimation.isChecked = SettingsManager.isSnowmanAnimationEnabled
        switchLabel.isChecked = SettingsManager.isLabelEnabled
        switchOpenLastDevice.isChecked = SettingsManager.isOpenLastDeviceEnabled
        switchSolidColorIcon.isChecked = SettingsManager.isSolidColorIconEnabled
        switchIconInLabel.isChecked = SettingsManager.isIconInLabelEnabled
        switchDoubleClickRefresh.isChecked = SettingsManager.isDoubleClickRefreshEnabled
        switchEasterEgg.isChecked = SettingsManager.isEasterEggEnabled
        switchBtnBack.isChecked = SettingsManager.isBackButtonEnabled
        switchAddressInput.isChecked = SettingsManager.isAddressInputEnabled
        switchPingDevice.isChecked = SettingsManager.isPingDevicesEnabled
        switchMarkInactive.isChecked = SettingsManager.isMarkInactive
        switchVibrato.isChecked = SettingsManager.isVibrato
        switchVibratoPing.isChecked = SettingsManager.isPingVibrato
        switchBlurHeader.isChecked = SettingsManager.isBlurHeader
        switchHistory.isChecked = SettingsManager.isHistory
        switchSwipeToRefresh.isChecked = SettingsManager.isSwipeToRefresh
        switchSwipeOpenPanel.isChecked = SettingsManager.isSwipeOpenPanel
        switchCheckNewVersion.isChecked = SettingsManager.isCheckNewVersionEnabled
        switchSplitNetwork.isChecked = SettingsManager.isSplitNetworkEnabled
        switchSplitNetworkManual.isChecked = SettingsManager.isSplitNetworkManualEnabled

        switchDontShowLocationDialog.isChecked = SettingsManager.isDontShowLocation


        val savedColor = SettingsManager.iconColor
        (colorPreview.background as GradientDrawable).setColor(savedColor.toColorInt())

        setupThemeSpinner()

        updateColorPickerVisibility(SettingsManager.isSolidColorIconEnabled)
        updateSplitNetworkVisibility(SettingsManager.isSplitNetworkEnabled, true)
        updateIconInLabelVisibility(SettingsManager.isLabelEnabled)

        val snowmanAnimationContainer = findViewById<LinearLayout>(R.id.switch_snowman_animation_container)
        if (SettingsManager.isSnowmanEnabled) snowmanAnimationContainer.visibility = View.VISIBLE
        else                                 snowmanAnimationContainer.visibility = View.GONE



        labelInfo.setOnClickListener {  view ->

            val textForTooltip = generateSpannedTextFor(TooltipType.LABEL)
            val balloon = createConfiguredBalloon(textForTooltip)
            balloon.showAlignBottom(view)

        }
        checkVersionInfo.setOnClickListener {  view ->

            val textForTooltip = generateSpannedTextFor(TooltipType.CHECK_NEW_VERSION)
            val balloon = createConfiguredBalloon(textForTooltip)
            balloon.showAlignBottom(view)

        }
        splitNetworkInfo.setOnClickListener {  view ->

            val textForTooltip = generateSpannedTextFor(TooltipType.SPLIT)
            val balloon = createConfiguredBalloon(textForTooltip)
            balloon.showAlignBottom(view)

        }
        dontLocationInfo.setOnClickListener {  view ->

            val textForTooltip = generateSpannedTextFor(TooltipType.NOT_GEO)
            val balloon = createConfiguredBalloon(textForTooltip)
            balloon.showAlignBottom(view)

        }
        splitManualInfo.setOnClickListener {  view ->

            val textForTooltip = generateSpannedTextFor(TooltipType.SPLIT_MANUAL)
            val balloon = createConfiguredBalloon(textForTooltip)
            balloon.showAlignBottom(view)

        }
        networksInfo.setOnClickListener {  view ->

            val textForTooltip = generateSpannedTextFor(TooltipType.NETWORKS)
            val balloon = createConfiguredBalloon(textForTooltip)
            balloon.showAlignBottom(view)

        }
        pingInfo.setOnClickListener { view ->

            val textForTooltip = generateSpannedTextFor(TooltipType.PING)
            val balloon = createConfiguredBalloon(textForTooltip)
            balloon.showAlignBottom(view)
        }

        historyInfo.setOnClickListener { view ->

            val textForTooltip = generateSpannedTextFor(TooltipType.HISTORY)
            val balloon = createConfiguredBalloon(textForTooltip)
            balloon.showAlignBottom(view)
        }

        swipeToRefreshInfo.setOnClickListener { view ->

            val textForTooltip = generateSpannedTextFor(TooltipType.SWIPE_REFRESH)
            val balloon = createConfiguredBalloon(textForTooltip)
            balloon.showAlignBottom(view)
        }

        swipeOpenPanelInfo.setOnClickListener { view ->
            val textForTooltip = generateSpannedTextFor(TooltipType.SWIPE_OPEN_PANEL)
            val balloon = createConfiguredBalloon(textForTooltip)
            balloon.showAlignBottom(view)
        }

        headerBlurInfo.setOnClickListener { view ->

            val textForTooltip = generateSpannedTextFor(TooltipType.HEADER_BLUR)
            val balloon = createConfiguredBalloon(textForTooltip)
            balloon.showAlignBottom(view)
        }




    }

    private fun setupThemeSpinner() {
        val themes = mutableListOf(
            getString(R.string.theme_default),
            getString(R.string.theme_mono),
            getString(R.string.theme_green),
            getString(R.string.theme_black),
            getString(R.string.theme_orange),
            getString(R.string.theme_red),
            getString(R.string.theme_palered),
            getString(R.string.theme_blue),
            getString(R.string.theme_bluelight),
            getString(R.string.theme_purple),
            getString(R.string.theme_purplegradient),
            getString(R.string.theme_yellowgreen),
            getString(R.string.theme_colorgradient),
            getString(R.string.theme_colors),
            getString(R.string.theme_gray)
            // getString(R.string.theme_yellow),
        )
        if (switchEasterEgg.isChecked)   themes.add(getString(R.string.theme_yellow))

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, themes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        themeSpinner.adapter = adapter

        // Устанавливаем текущую тему
        themeSpinner.setSelection(ThemeManager.getThemeSpinnerPosition(this))
    }


    private fun setupListeners() {
        // Обработчик кнопки "Назад"
        backButton.setOnClickListener {
            finish()
        }

        // Слушатель для чекбокса "Снеговик"
        switchSnowman.setOnCheckedChangeListener { buttonView, isChecked ->
            findViewById<LinearLayout>(R.id.switch_snowman_animation_container).visibility = if (isChecked) View.VISIBLE else View.GONE

            // Проверяем, что событие вызвано реальным нажатием пользователя
            if (buttonView.isPressed) {
                // Показываем диалог, передавая ему состояние ДО нажатия
                showIconChangeDialog(!isChecked)
            }
        }

        // Слушатель для чекбокса "Анимация Снеговик"
        switchSnowmanAnimation.setOnCheckedChangeListener { _, isChecked ->
            SettingsManager.isSnowmanAnimationEnabled = isChecked
        }

        // Слушатель для чекбокса "Пасхалочка"
        switchEasterEgg.setOnCheckedChangeListener { _, isChecked ->
            SettingsManager.isEasterEggEnabled = isChecked
            setupThemeSpinner() // Пересобираем список тем с учетом пасхалки
            if (isChecked) {
                showEasterEggMessage()
            }
        }

        // Слушатель для чекбокса "Кнопка назад"
        switchBtnBack.setOnCheckedChangeListener { switchView, isChecked ->
            if (switchView.isPressed) {
                SettingsManager.isBackButtonEnabled = isChecked
            }

        }

        // Слушатель для чекбокса "Открывать последнее устройство при старте"
        switchOpenLastDevice.setOnCheckedChangeListener { _, isChecked ->
            SettingsManager.isOpenLastDeviceEnabled = isChecked
        }


        switchAddressInput.setOnCheckedChangeListener { _, isChecked ->
            SettingsManager.isAddressInputEnabled = isChecked
        }
        switchPingDevice.setOnCheckedChangeListener { _, isChecked ->
            SettingsManager.isPingDevicesEnabled = isChecked
            if(isChecked) {
                findViewById<LinearLayout>(R.id.mark_inactive_devices_layout).visibility = View.VISIBLE
            }
            else {
                findViewById<LinearLayout>(R.id.mark_inactive_devices_layout).visibility = View.GONE
            }
        }

        switchMarkInactive.setOnCheckedChangeListener { _, isChecked ->
            SettingsManager.isMarkInactive = isChecked
        }
        switchVibrato.setOnCheckedChangeListener { _, isChecked ->
            SettingsManager.isVibrato = isChecked
        }
        switchVibratoPing.setOnCheckedChangeListener { _, isChecked ->
            SettingsManager.isPingVibrato = isChecked
        }


        switchBlurHeader.setOnCheckedChangeListener { _, isChecked ->
            SettingsManager.isBlurHeader = isChecked
            SettingsManager.themeWasChanged = true // чтобы применить изменения
        }

        switchHistory.setOnCheckedChangeListener { _, isChecked ->
            SettingsManager.isHistory = isChecked
        }

        switchSwipeToRefresh.setOnCheckedChangeListener { _, isChecked ->
            SettingsManager.isSwipeToRefresh = isChecked
        }

        switchSwipeOpenPanel.setOnCheckedChangeListener { _, isChecked ->
            SettingsManager.isSwipeOpenPanel = isChecked
        }

        // Слушатель для чекбокса "Проверять новую версию"
        switchCheckNewVersion.setOnCheckedChangeListener { _, isChecked ->
            SettingsManager.isCheckNewVersionEnabled = isChecked
        }

        // Слушатель для чекбокса "Разделять сети по SSID"
        switchSplitNetwork.setOnCheckedChangeListener { _, isChecked ->
            val currentSplitNetwork = SettingsManager.isSplitNetworkEnabled
            SettingsManager.isSplitNetworkEnabled = isChecked
            val silent = currentSplitNetwork == isChecked
            updateSplitNetworkVisibility(isChecked, silent)
        }

        // Слушатель для чекбокса "Разделять сети вручную"
        switchSplitNetworkManual.setOnCheckedChangeListener { _, isChecked ->
            SettingsManager.isSplitNetworkManualEnabled = isChecked
            updateDontShowLocation(isChecked)
        }
        // Слушатель для чекбокса "не показывать запрос геолокации"
        switchDontShowLocationDialog.setOnCheckedChangeListener { _, isChecked ->
            SettingsManager.isDontShowLocation = isChecked
        }

        // Слушатель для чекбокса "Ярлык"
        switchLabel.setOnCheckedChangeListener { switchView, isChecked ->
            if (switchView.isPressed) {
                SettingsManager.isLabelEnabled = isChecked
                switchBtnBack.isChecked = true
            }

            updateIconInLabelVisibility(isChecked)
        }

        // Слушатель для чекбокса "Иконка в ярлыке"
        switchIconInLabel.setOnCheckedChangeListener { _, isChecked ->
            SettingsManager.isIconInLabelEnabled = isChecked
        }
        // Слушатель для чекбокса "Иконка в ярлыке"
        switchDoubleClickRefresh.setOnCheckedChangeListener { _, isChecked ->
            SettingsManager.isDoubleClickRefreshEnabled = isChecked
        }

        // Слушатель для чекбокса "Однотонный цвет иконки"
        switchSolidColorIcon.setOnCheckedChangeListener { _, isChecked ->
            SettingsManager.isSolidColorIconEnabled = isChecked
            updateColorPickerVisibility(isChecked)
        }

        // Обработчик кнопки выбора цвета
        colorPickerButton.setOnClickListener {
            showColorPickerDialog()
        }

        // Слушатель для выбора темы
        themeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedTheme = ThemeManager.getThemeNameByPosition(position)
                val previousTheme = SettingsManager.appTheme //  старая тема

                if (selectedTheme != previousTheme) {

                    SettingsManager.appTheme = selectedTheme
                    SettingsManager.themeWasChanged = true

                    // Применяем тему к текущей Activity
                    ThemeManager.applyTheme(this@SettingsActivity)

                    Toast.makeText(
                        this@SettingsActivity,
                        "Тема изменена на: ${ThemeManager.getThemeDisplayName(this@SettingsActivity)}",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Получаем цвет иконки из новой темы и сохраняем его
                    val typedArray = obtainStyledAttributes(intArrayOf(R.attr.textDeviceIconsTint))
                    val color = typedArray.getColor(0, Color.DKGRAY)
                    typedArray.recycle()
                    val hexColor = String.format("#%08X", color)
                    SettingsManager.iconColor = hexColor

                    // Пересоздаем текущую Activity для применения стилей
                    recreate()
                    WindowCompat.setDecorFitsSystemWindows(window, false)
                    window.statusBarColor = Color.TRANSPARENT
                    window.navigationBarColor = Color.TRANSPARENT
                }

                Log.d("ТЕМА", "selectedTheme = $selectedTheme, previousTheme = $previousTheme")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Ничего не делаем
            }
        }

        // Вызываем настройку списка сетей
        setupNetworksRecyclerView()
    }

    private fun showIconChangeDialog(previousCheckedState: Boolean) {
        // Создаем диалоговое окно AlertDialog
        AlertDialog.Builder(this, R.style.AppTheme_RoundedAlertDialog)
            .setTitle("Смена Снеговика")
            .setMessage("Для применения изменений приложение будет закрыто. Вы уверены?")


            .setPositiveButton("Да, применить") { dialog, which ->
                // Пользователь согласился. Сохраняем новое состояние и меняем иконку.
                val newState = !previousCheckedState // Новое состояние - обратное предыдущему

                SettingsManager.isSnowmanEnabled = newState

                if(newState) ThemeManager.setTheme(this, "yellow")
                else ThemeManager.setTheme(this, "default")

                updateAppIcon()
            }


            .setNegativeButton("Нет, отмена") { dialog, which ->
                // Пользователь отказался. Возвращаем переключатель в исходное состояние.
                switchSnowman.isChecked = previousCheckedState
            }

            // Добавляем обработчик отмены диалога (нажатие вне окна или кнопка "назад")
            .setOnCancelListener {
                // Если пользователь отменил диалог, это равносильно нажатию "Нет".
                switchSnowman.isChecked = previousCheckedState
            }

            // Показываем созданный диалог
            .show()
    }






    private fun showEasterEggMessage() {
        // посхал очка
        return
        android.widget.Toast.makeText(
            this,
            "🎉 Ура! Вы нашли посхалочку!",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun applyThemeImmediately(theme: String) {

        Toast.makeText(
            this,
            "Тема изменена. Применение темы.",
            Toast.LENGTH_SHORT
        ).show()

        recreate()
    }

    private fun updateColorPickerVisibility(isSolidColorEnabled: Boolean) {
        // Скрываем кнопку выбора цвета когда опция отключена
        colorPickerButton.visibility = if (isSolidColorEnabled) View.VISIBLE else View.GONE
    }

    private fun updateIconInLabelVisibility(isLabelEnabled: Boolean) {
        // Скрываем чекбокс "Иконка в ярлыке" когда опция "Ярлык" отключена
        iconInLabelLayout.visibility = if (isLabelEnabled) View.VISIBLE else View.GONE
        doubleClickRefreshLayout.visibility = if (isLabelEnabled) View.VISIBLE else View.GONE

        if(switchIconInLabel.isChecked) iconInLabelLayout.visibility = View.GONE

        val buttonBack = findViewById<LinearLayout>(R.id.back_button_expand_layout)
        val buttonBackLabel = findViewById<TextView>(R.id.back_button_expand_label)
        // buttonBack.visibility = if (isLabelEnabled) View.VISIBLE else View.GONE
        if (isLabelEnabled) {
            switchBtnBack.isEnabled = true
            switchBtnBack.isActivated = true

            buttonBackLabel.setTextColor("#736E6E".toColorInt())
        }
        else {
            switchBtnBack.isEnabled = false
            switchBtnBack.isActivated = false

            buttonBackLabel.setTextColor("#c4c4c4".toColorInt())

        }
    }

    private fun updateSplitNetworkVisibility(isLabelEnabled: Boolean, silent: Boolean = false) {

        findViewById<LinearLayout>(R.id.management_network_container).visibility = if (isLabelEnabled) View.VISIBLE else View.GONE

        if(isLabelEnabled && !silent)  showSplitNetworkDialog()

    }
    private fun updateDontShowLocation(isManualSplit: Boolean){
        if (isManualSplit) dontShowLocationDialogContainer.visibility = View.GONE else dontShowLocationDialogContainer.visibility = View.VISIBLE
    }

    private fun showSplitNetworkDialog() {
        val message = """
            Для определения имени Wi-Fi сети приложению нужен доступ к геолокации.
            <br><br>
            <b>Есть два режима разделения:</b>
            <br><br>
            &#8226; <b>Автоматический</b><br>
            Приложение будет само определять текущую Wi-Fi сеть и показывать список устройств для нее. При обнаружении новой сети предложит ее добавить.
            <br><br>
            &#8226; <b>Ручной</b><br>
            Вы должны вручную создавать сети и переключаться между ними. Этот режим не требует разрешений.
            <br><br>
            <small><i><b>RSM</b> не собирает и не использует данные о вашем местоположении. Эти разрешения необходимы только для определения имени и параметров WiFi сети.</i></small>
        """.trimIndent()


        AlertDialog.Builder(this, R.style.AppTheme_RoundedAlertDialog)
            .setTitle("Разделять устройства по сетям?")
            .setMessage(Html.fromHtml(message, Html.FROM_HTML_MODE_LEGACY))

            // "Хорошо" -> Включаем автоматический режим
            .setPositiveButton("Хорошо") { _, _ ->
                SettingsManager.isSplitNetworkManualEnabled = false
                switchSplitNetworkManual.isChecked = false
            }

            // "Отмена" -> Выключаем разделение сетей полностью
            .setNegativeButton("Отмена") { _, _ ->
                SettingsManager.isSplitNetworkEnabled = false
                switchSplitNetwork.isChecked = false
                updateSplitNetworkVisibility(false, true)
            }

            // "Разделять вручную" -> Включаем ручной режим
            .setNeutralButton("Разделять вручную"){ _, _ ->
                SettingsManager.isSplitNetworkManualEnabled = true
                switchSplitNetworkManual.isChecked = true
            }

            // Отмена диалога
            .setOnCancelListener {
                SettingsManager.isSplitNetworkEnabled = false
                switchSplitNetwork.isChecked = false
                updateSplitNetworkVisibility(false, true)
            }
            .show()
    }

    private fun showColorPickerDialog() {

        val currentColor = SettingsManager.iconColor
        ColorPickerDialog.Builder(this)
            .setTitle("Выберите цвет")
            .setPreferenceName("MyColorPickerDialog")
            .setPositiveButton("OK", ColorEnvelopeListener { envelope, _ ->
                // Обрабатываем выбранный цвет
                val colorInt = envelope.color

                // Меняем цвет у drawable
                val drawable = colorPreview.background as GradientDrawable
                drawable.setColor(colorInt)

                // Сохраняем выбранный цвет в формате HEX
                val hexColor = String.format("#%06X", 0xFFFFFF and colorInt)
                SettingsManager.iconColor = hexColor


                // Показываем сообщение об успешном выборе
                android.widget.Toast.makeText(this, "Цвет изменен", android.widget.Toast.LENGTH_SHORT).show()
            })
            .setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss()
            }
            .attachAlphaSlideBar(true) // ползунок прозрачности
            .attachBrightnessSlideBar(true) // ползунок яркости
            .setBottomSpace(12) // отступ снизу
            .show()
    }


    fun buttonAbout(view: View) {
        val aboutWindow = findViewById<FrameLayout>(R.id.about_layout)
        val dialogContent = aboutWindow.findViewById<com.lihang.ShadowLayout>(R.id.about_dialog_content)
        val appVersionTextView: TextView = findViewById(R.id.app_version)
        val versionName = BuildConfig.VERSION_NAME
        appVersionTextView.text = versionName

        // Сбрасываем анимации
        aboutWindow.clearAnimation()
        dialogContent.clearAnimation()

        if(switchSnowman.isChecked) {
            val appName = findViewById<TextView>(R.id.app_name)
            appName.text = "Red Snow Men"
            findViewById<ImageView>(R.id.icon).setImageResource(R.mipmap.ic_launcher_snow)
        }


        // Показываем окно
        aboutWindow.visibility = View.VISIBLE

        // Анимация появления
        val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        dialogContent.startAnimation(animation)
    }

    fun closeAbout(view: View) {
        val aboutWindow = findViewById<FrameLayout>(R.id.about_layout)
        val dialogContent = aboutWindow.findViewById<com.lihang.ShadowLayout>(R.id.about_dialog_content)

        // Сбрасываем анимации
        aboutWindow.clearAnimation()
        dialogContent.clearAnimation()

        // Анимация исчезновения
        val animation = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        dialogContent.startAnimation(animation)

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                // Скрываем окно - теперь основной контент будет доступен
                aboutWindow.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }








    private fun setupNetworksRecyclerView() {
        networksRecyclerView = findViewById(R.id.networks_recycler_view)
        val clearAllButton: TextView = findViewById(R.id.clear_all_networks_button)

        //  Получаем список сетей по новой модели KnownNetwork
        val networks = NetworkManager.getKnownNetworks(this).toMutableList()
        Log.d("SettingsActivity", "Known networks: $networks")

        //  Создаем и устанавливаем адаптер, который работает с KnownNetwork
        //networkAdapter = NetworkAdapter(networks) { networkToDelete ->
        //    // Этот блок вызывается при нажатии на кнопку удаления в адаптере
        //    showDeleteConfirmationDialog(networkToDelete)
        //}
        networkAdapter = NetworkAdapter(
            networks = networks,
            onDeleteClick = { networkToDelete ->
                showDeleteConfirmationDialog(networkToDelete)
            },
            onRenameClick = { networkToRename ->
                showRenameNetworkDialog(networkToRename)
            },
            onTrustClick = { networkToTrust ->
                showTrustNetworkDialog(networkToTrust)
            },
            onEditClick = { networkToEdit ->
                showEditNetworkDialog(networkToEdit)
            }
        )

        networksRecyclerView.layoutManager = LinearLayoutManager(this)
        networksRecyclerView.adapter = networkAdapter

        // Обработчик кнопки "Удалить все сети"
        clearAllButton.setOnClickListener {
            showClearAllConfirmationDialog()
        }
    }

    private fun showDeleteConfirmationDialog(network: KnownNetwork) {
        val deviceCount = deviceManager.getDeviceCountForNetwork(network.fingerprint)
        val message = "Вы уверены, что хотите удалить сеть <b>${network.name}</b>?<br>Это также удалит и все устройства в сети (<b>${deviceCount}</b> шт.)."
        val toastMsg = if(deviceCount == 0) "Сеть удалена" else "Сеть и устройства удалены"

        AlertDialog.Builder(this, R.style.AppTheme_RoundedAlertDialogErr)

            .setTitle("Удаление сети")
            .setMessage(Html.fromHtml(message, Html.FROM_HTML_MODE_LEGACY))
            .setPositiveButton("Да, удалить") { _, _ ->
                NetworkManager.removeNetwork(this, network)
                networkAdapter.removeItem(network)
                Toast.makeText(this, toastMsg, Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showClearAllConfirmationDialog() {
        AlertDialog.Builder(this, R.style.AppTheme_RoundedAlertDialogErr)
            .setTitle("Очистка списка")
            .setMessage("Вы уверены, что хотите удалить все сохраненные сети?")
            .setPositiveButton("Да, удалить все") { _, _ ->

                NetworkManager.clearAll(this)
                networkAdapter.clearAll()
                Toast.makeText(this, "Все сети удалены", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }


    inner class NetworkAdapter(
        private val networks: MutableList<KnownNetwork>,
        private val onDeleteClick: (KnownNetwork) -> Unit,
        private val onRenameClick: (KnownNetwork) -> Unit,
        private val onTrustClick: (KnownNetwork) -> Unit,
        private val onEditClick: (KnownNetwork) -> Unit
    ) : RecyclerView.Adapter<NetworkAdapter.NetworkViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NetworkViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_network, parent, false)
            return NetworkViewHolder(view)
        }

        override fun onBindViewHolder(holder: NetworkViewHolder, position: Int) {
            val network = networks[position]
            holder.bind(network)
        }


        fun updateItemName(network: KnownNetwork, newName: String) {
            val position = networks.indexOf(network)
            if (position > -1) {
                // Обновляем имя в объекте
                networks[position].name = newName
                // Уведомляем адаптер, что этот конкретный элемент изменился
                notifyItemChanged(position)
            }
        }

        override fun getItemCount(): Int = networks.size

        fun removeItem(network: KnownNetwork) {
            val position = networks.indexOf(network)
            if (position > -1) {
                networks.removeAt(position)
                notifyItemRemoved(position)
            }
            val fingerprint = network.fingerprint
            deviceManager.clearDevices(fingerprint)

        }

        fun clearAll() {
            val size = networks.size
            networks.clear()
            notifyItemRangeRemoved(0, size)
        }

        inner class NetworkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val ssidTextView: TextView = itemView.findViewById(R.id.network_ssid)
            private val bssidTextView: TextView = itemView.findViewById(R.id.network_bssid) // !!! fingerprint
            private val trustedIcon: ImageView = itemView.findViewById(R.id.trusted_status_icon)
            private val deleteButton: ImageView = itemView.findViewById(R.id.delete_network_button)
            private val editButton: ImageView = itemView.findViewById(R.id.edit_network_button)

            fun bind(network: KnownNetwork) {
                ssidTextView.text = network.name
                val ssid = network.ssid
                val deviceCount = deviceManager.getDeviceCountForNetwork(network.fingerprint)
                val builder = SpannableStringBuilder()

                val deviceCountText = "Устройства: "
                builder.append(deviceCountText)
                builder.setSpan(StyleSpan(Typeface.BOLD), builder.length - deviceCountText.length, builder.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                builder.append("$deviceCount шт.\n")

                val ssidLabel = "SSID: "
                builder.append(ssidLabel)
                builder.setSpan(StyleSpan(Typeface.BOLD), builder.length - ssidLabel.length, builder.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                builder.append(ssid ?: "N/A").append("\n")

                // Основной MAC-префикс
                val mainBssid = network.mainBssidPrefix
                if (mainBssid.isNotBlank()) {
                    val bssidLabel = "MAC-префикс: "
                    builder.append(bssidLabel)
                    builder.setSpan(StyleSpan(Typeface.BOLD), builder.length - bssidLabel.length, builder.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    builder.append(mainBssid).append("\n")
                } else {

                    val fingerprintParts = network.fingerprint.split(';')
                    val fallbackBssid = fingerprintParts.find { it.startsWith("bssid_prefix=") }?.removePrefix("bssid_prefix=")
                    if (fallbackBssid != null) {
                        val bssidLabel = "MAC-префикс: "
                        builder.append(bssidLabel)
                        builder.setSpan(StyleSpan(Typeface.BOLD), builder.length - bssidLabel.length, builder.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        builder.append(fallbackBssid).append("\n")
                    }
                }



                // Подсеть
                if (network.subnet != null) {
                    val subnetLabel = "Подсеть: "
                    builder.append(subnetLabel)
                    builder.setSpan(StyleSpan(Typeface.BOLD), builder.length - subnetLabel.length, builder.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    builder.append(network.subnet).append("\n")
                }

                // Дополнительные точки доступа
                val additionalList = network.additionalBssids
                if (additionalList.isNotEmpty()) {
                    val additionalLabel = if (additionalList.size == 1) "Доп. точка: " else "Доп. точки "
                    builder.append(additionalLabel)
                    builder.setSpan(StyleSpan(Typeface.BOLD), builder.length - additionalLabel.length, builder.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

                    if (additionalList.size in 2..5) builder.append("${additionalList.size} шт.:  \n  ")
                    // Если точек мало — показываем их, иначе только количество
                    if (additionalList.size <= 5) {
                        builder.append(additionalList.joinToString("\n  "))
                    } else {
                        builder.append("${additionalList.size} шт.")
                    }
                    builder.append("\n")
                }

                // Убираем последний перенос строки
                if (builder.isNotEmpty() && builder.last() == '\n') {
                    builder.delete(builder.length - 1, builder.length)
                }

                bssidTextView.text = builder

                // Иконка доверия
                trustedIcon.visibility = View.VISIBLE
                trustedIcon.setImageResource(
                    if (network.isTrusted) R.drawable.ic_trusted_network
                    else R.drawable.ic_untrusted_network
                )



                // Слушатели
                ssidTextView.setOnClickListener {
                    onRenameClick(network)
                }

                deleteButton.setOnClickListener {
                    onDeleteClick(network)
                }
                editButton.setOnClickListener {
                    onEditClick(network)
                }
                trustedIcon.setOnClickListener {
                    onTrustClick(network)
                }
            }
        }
    }

    private fun showRenameNetworkDialog(network: KnownNetwork) {
        // Создаем EditText для ввода нового имени
        val editText = EditText(this).apply {
            // Устанавливаем текущее имя сети в поле для редактирования
            setText(network.name)
            setPadding(32.dpToPx(),16.dpToPx(), 32.dpToPx(), 16.dpToPx())
        }

        // Создаем и настраиваем диалоговое окно
        AlertDialog.Builder(this, R.style.AppTheme_RoundedAlertDialog)
            .setTitle("Переименовать сеть")
            .setView(editText) // Добавляем EditText в диалог
            .setPositiveButton("Сохранить") { _, _ ->
                val newName = editText.text.toString().trim()

                if (newName.isNotEmpty()) {
                    // Вызываем функцию из TrustedNetworkManager для обновления
                    NetworkManager.updateNetworkName(this, network.id, newName)
                    Toast.makeText(this, "Имя обновлено", Toast.LENGTH_SHORT).show()
                    // Обновляем данные в адаптере, чтобы увидеть изменения
                    networkAdapter.updateItemName(network, newName)
                } else {
                    Toast.makeText(this, "Имя не может быть пустым", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showTrustNetworkDialog(network: KnownNetwork) {
        // Создаем EditText для ввода нового имени
        // val editText = EditText(this).apply {
        //     // Устанавливаем текущее имя сети в поле для редактирования
        //     setText(network.isTrusted)
        // }
        // val buttonTrust = Button(this).apply {
        //
        // }


        AlertDialog.Builder(this, R.style.AppTheme_RoundedAlertDialog)
            .setTitle("Доверие всем сертификатам")
            .setMessage("При сканировании локальной сети, RSM не будет проверять сертификаты https.\n\n" +
                    "Потенциально не безопасная настройка, если в вашей сети нет устройств с самоподписанными сертификатами, выбирайте - Нет")

            .setPositiveButton("Доверять") { _, _ ->

                val trust = NetworkManager.updateNetworkTrustStatus(this, network.id, true)

                if (trust == null) Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show()
                else if(trust) {
                    Toast.makeText(this, "Изменили на доверять", Toast.LENGTH_SHORT).show()
                    recreate()
                }
                else Toast.makeText(this, "Ничего не изменилось", Toast.LENGTH_SHORT).show()

            }
            .setNegativeButton("Нет"){ _, _ ->

                val trust = NetworkManager.updateNetworkTrustStatus(this, network.id, false)
                if (trust == null) Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show()
                else if(trust) {
                    Toast.makeText(this, "Изменили на не доверять", Toast.LENGTH_SHORT).show()
                    recreate()
                }
                else Toast.makeText(this, "Ничего не изменилось", Toast.LENGTH_SHORT).show()
            }
            .show()
    }












    @SuppressLint("SetTextI18n")
    private fun showEditNetworkDialog(network: KnownNetwork) {
        val context = this
        val allNetworks = NetworkManager.loadNetworks(context)
        val otherNetworks = allNetworks.filter { it.id != network.id }

        val scrollView = ScrollView(context)
        val mainLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            val padding = 16.dpToPx()
            setPadding(padding, padding / 2, padding, padding / 2)
        }
        scrollView.addView(mainLayout)




        val massage = TextView(context).apply {

            val htmlText = "<font color='#FF0000'><b>Внимание!</b></font><br>" +
                    "<b>SSID</b>, <b>MAC-префикс</b> и <b>подсеть</b>, участвуют в определении сети.<br>" +
                    "Изменение этих параметров может привести к потере связи между сетью и устройствами.<br>" +
                    "А так же в режиме авторазделения RSM перестанет узнавать эту сеть.<br><br>" +
                    "⚠️ <i>Меняйте эти параметры с осторожностью.</i>"

            text = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)


            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                val margin = 16.dpToPx()
                setMargins(margin, 0, margin, margin)
            }

            textSize = 12f
        }



        val editName = createFieldWithLabel("Имя:", network.name, "Имя сети")
        val editSsid = createFieldWithLabel("SSID:", network.ssid, "Название Wi-Fi")
        val editBssid = createFieldWithLabel("MAC-префикс:", network.mainBssidPrefix, "AA:BB:CC:DD:EE")
        val editSubnet = createFieldWithLabel("Подсеть:", network.subnet ?: "", "192.168.1.0/24 (необязательно)")
        mainLayout.addView(massage)
        mainLayout.addView(editName.first)
        mainLayout.addView(editSsid.first)
        mainLayout.addView(editBssid.first)
        mainLayout.addView(editSubnet.first)

        val trustCheckBox = CheckBox(context).apply {
            text = "Не проверять сертификаты htpps"
            isChecked = network.isTrusted
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply { topMargin = 16.dpToPx() }
        }
        mainLayout.addView(trustCheckBox)

        val hideNetworkCheckBox = CheckBox(context).apply {
            text = "Скрыть сеть в выпадающем списке в шторке"
            isChecked = network.isHided
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply { topMargin = 16.dpToPx() }
        }
        mainLayout.addView(hideNetworkCheckBox)

        val divider = View(context).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1.dpToPx()).apply { topMargin = 16.dpToPx(); bottomMargin = 8.dpToPx() }
            setBackgroundColor(Color.GRAY)
        }
        mainLayout.addView(divider)
        val additionalBssidsTitle = TextView(context).apply {
            text = "MAC-Префикс'ы дополнительных точек:"
            setTypeface(null, Typeface.BOLD)
        }
        mainLayout.addView(additionalBssidsTitle)
        val additionalBssidsLayout = LinearLayout(context).apply { orientation = LinearLayout.VERTICAL }
        mainLayout.addView(additionalBssidsLayout)
        fun refreshBssidList() {
            additionalBssidsLayout.removeAllViews()
            if (network.additionalBssids.isEmpty()) {
                val emptyView = TextView(context).apply { text = "  (список пуст)" }
                additionalBssidsLayout.addView(emptyView)
            } else {
                network.additionalBssids.toSet().sorted().forEach { bssid ->
                    val bssidRow = LinearLayout(context).apply { orientation = LinearLayout.HORIZONTAL; gravity = android.view.Gravity.CENTER_VERTICAL }
                    val bssidText = TextView(context).apply { text = bssid; layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f) }
                    val deleteButton = Button(context).apply { text = "✕"; setOnClickListener { network.additionalBssids.remove(bssid); refreshBssidList() } }
                    bssidRow.addView(bssidText)
                    bssidRow.addView(deleteButton)
                    additionalBssidsLayout.addView(bssidRow)
                }
            }
        }
        refreshBssidList()


        lateinit var editDialog: AlertDialog // Объявляем переменную для диалога
        val editDialogBuilder = AlertDialog.Builder(this, R.style.AppTheme_RoundedAlertDialog)
            .setTitle("Редактировать сеть")
            .setView(scrollView)
            .setPositiveButton("Сохранить") { _, _ ->
                val newName = editName.second.text.toString().trim()
                val newSsid = editSsid.second.text.toString().trim()
                val newBssid = editBssid.second.text.toString().trim()
                val newSubnet = editSubnet.second.text.toString().trim().ifEmpty { null }
                val newTrustStatus = trustCheckBox.isChecked
                val newHideStatus = hideNetworkCheckBox.isChecked

                if (newName.isNotEmpty() && newSsid.isNotEmpty() && newBssid.isNotEmpty()) {

                    if(network.name != newName) {
                        deviceManager.renameNetworkDevices(network.fingerprint, newName)
                    }
                    network.name = newName
                    network.ssid = newSsid
                    network.mainBssidPrefix = newBssid
                    network.subnet = newSubnet
                    network.isTrusted = newTrustStatus
                    network.isHided = newHideStatus
                    network.additionalBssids = network.additionalBssids.toSet().toMutableList()
                    NetworkManager.updateWholeNetwork(context, network)





                    Toast.makeText(this, "Сеть обновлена", Toast.LENGTH_SHORT).show()




                    recreate()

                } else {
                    Toast.makeText(this, "Имя, SSID и MAC-префикс не могут быть пустыми", Toast.LENGTH_SHORT).show()
                }


            }
            .setNegativeButton("Отмена"){ _, _ ->
                recreate()
            }
            .setNeutralButton("Объединить...") { _, _ ->

            }

        // Показываем диалог и сохраняем на него ссылку
        editDialog = editDialogBuilder.create()
        editDialog.show()

        // Назначаем свой обработчик для кнопки "Объединить" ПОСЛЕ показа диалога
        editDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener {
            showMergeSelectionDialog(network, otherNetworks, editDialog) // Передаем сам диалог
        }
    }

    /**
     * Показывает диалог для выбора сети для слияния.
     * @param currentNetwork Сеть, В которую будут добавлены данные.
     * @param candidates Список сетей, ИЗ которых можно выбрать для слияния.
     * @param parentDialog Диалог редактирования, который нужно закрыть перед recreate().
     */
    private fun showMergeSelectionDialog(currentNetwork: KnownNetwork, candidates: List<KnownNetwork>, parentDialog: AlertDialog) {
        if (candidates.isEmpty()) {
            Toast.makeText(this, "Нет других сетей для объединения", Toast.LENGTH_SHORT).show()
            return
        }

        val networkNames = candidates.map { it.name }.toTypedArray()

        AlertDialog.Builder(this, R.style.AppTheme_RoundedAlertDialog)
            .setTitle("Объединить с...")

            .setItems(networkNames) { selectionDialog, which ->
                val networkToMerge = candidates[which]
                val message = """
                    Вы уверены, что хотите объединить <b>${currentNetwork.name}</b> (редактируемая) с <b><i>${networkToMerge.name}</i></b>?<br>
                    Сеть <b><i>${networkToMerge.name}</i></b> будет удалена а все устройства перемещены в редактируемую.
                    """.trimIndent()
                // Показываем диалог подтверждения
                AlertDialog.Builder(this, R.style.AppTheme_RoundedAlertDialog)
                    .setTitle("Подтверждение")
                    .setMessage(Html.fromHtml(message, Html.FROM_HTML_MODE_LEGACY))
                    .setPositiveButton("Да") { _, _ ->
                        // Сначала закрываем диалог редактирования
                        parentDialog.dismiss()

                        val success =  deviceManager.moveDevices(networkToMerge.fingerprint, currentNetwork.fingerprint)

                        val textSuccess = if(success) ", устройства перемещены" else ""
                        // Выполняем слияние
                        val mergeResult = NetworkManager.mergeNetworks(this, targetNetworkId = currentNetwork.id, sourceNetworkId = networkToMerge.id)
                        if (mergeResult) {
                            Toast.makeText(this, "Сети объединены${textSuccess}", Toast.LENGTH_SHORT).show()
                            recreate() // Пересоздаем всю активность для обновления списка
                        } else {
                            Toast.makeText(this, "Ошибка при объединении сетей", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("Нет", null)
                    .show()

                selectionDialog.dismiss()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    /**
     * Вспомогательная функция для создания строки "Подпись - Поле ввода".
     * @return Pair, где first - это LinearLayout (строка), а second - это EditText (поле ввода).
     */
    private fun createFieldWithLabel(label: String, initialText: String, hint: String): Pair<LinearLayout, EditText> {
        val context = this
        val rowLayout = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 4.dpToPx()
                bottomMargin = 4.dpToPx()
            }
            gravity = android.view.Gravity.CENTER_VERTICAL
        }
        val textView = TextView(context).apply {
            text = label
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { marginEnd = 8.dpToPx() }
        }
        val editText = EditText(context).apply {
            this.hint = hint
            setText(initialText)
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
        }
        rowLayout.addView(textView)
        rowLayout.addView(editText)
        return Pair(rowLayout, editText)
    }
















    fun openGitHub(view: View) {
        val url = "https://github.com/TonTon-Macout/Remote-Settings-Manager/blob/main/android/README.md"

        // Создаем диалоговое окно для выбора
        AlertDialog.Builder(this, R.style.AppTheme_RoundedAlertDialog)
            .setTitle("Открыть ссылку") // "Открыть ссылку"
            .setMessage("Где открыть ссылку на GitHub?")
            .setPositiveButton("В браузере") { _, _ ->
                // Опция "В браузере"
                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                startActivity(intent)
            }
            .setNegativeButton("В приложении") { _, _ ->
                // Опция "В приложении"
                // Создаем Intent для возврата в MainActivity
                val intent = Intent(this, MainActivity::class.java).apply {
                    // Добавляем URL в Intent
                    putExtra("URL_TO_LOAD", url)
                    // Флаги для очистки стека и создания новой задачи
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                }
                startActivity(intent)
                finish() // Закрываем настройки после выбора
            }
            .setNeutralButton("Отмена", null) // Кнопка "Отмена"
            .show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    override fun onResume() {
        super.onResume()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()

    }
    override fun onDestroy() {
        super.onDestroy()

    }

    private fun updateAppIcon() {
        val useSnowman = switchSnowman.isChecked && switchEasterEgg.isChecked

        try {
            val packageManager = packageManager
            val packageName = packageName


            //val defaultAlias = ComponentName(packageName, ".MainActivityDefault")
            //val snowAlias = ComponentName(packageName, ".MainActivitySnow")

            val defaultAlias = ComponentName(packageName, "$packageName.MainActivityDefault")
            val snowAlias = ComponentName(packageName, "$packageName.MainActivitySnow")

            if (useSnowman) {
                // Включаем "Снеговика", выключаем "Обычную" иконку
                packageManager.setComponentEnabledSetting(
                    snowAlias,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
                )
                packageManager.setComponentEnabledSetting(
                    defaultAlias,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                Toast.makeText(this, "Ура! 🎉 \nRed Snow Man активирован!", Toast.LENGTH_LONG).show()
            } else {
                // Включаем "Обычную" иконку, выключаем "Снеговика"
                packageManager.setComponentEnabledSetting(
                    defaultAlias,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
                )
                packageManager.setComponentEnabledSetting(
                    snowAlias,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                // Можно добавить Toast о возвращении стандартной иконки, если нужно
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Ошибка смены иконки", Toast.LENGTH_SHORT).show()
        }
    }

    fun clearAllWebViewData(view: View) {
        AlertDialog.Builder(this, R.style.AppTheme_RoundedAlertDialogErr)
            .setTitle("Очистить данные?")
            .setMessage("Это удалит все куки, кеш и данные веб-хранилищ. Вам может потребоваться повторная авторизация на устройствах.")
            .setPositiveButton("Очистить") { _, _ ->
                try {
                    //  куки
                    val cookieManager = CookieManager.getInstance()
                    cookieManager.removeAllCookies { success ->
                        // колбек
                    }
                    cookieManager.flush()

                    //  веб-хранилище
                    WebStorage.getInstance().deleteAllData()

                    //  кеш файлов, форм и истории
                    val tempWebView = WebView(this)
                    tempWebView.clearCache(true)
                    tempWebView.clearFormData()
                    tempWebView.clearHistory()
                    tempWebView.destroy()

                    Toast.makeText(this, "Кеш и данные очищены", Toast.LENGTH_SHORT).show()


                    SettingsManager.themeWasChanged = true//  чтобы обновить страницу
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Ошибка при очистке: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun generateSpannedTextFor(tooltipType: TooltipType): Spanned {
        val imageGetter = Html.ImageGetter { source ->
            val drawableId = when (source) {
                "wifi_icon"    -> R.drawable.ic_wifi_circle

                else -> 0
            }
            if (drawableId != 0) {
                val drawable = ContextCompat.getDrawable(this, drawableId)!!
                val iconSize = (15f * resources.displayMetrics.density).toInt()
                drawable.setBounds(0, 0, iconSize, iconSize)
                return@ImageGetter drawable
            }
            return@ImageGetter null
        }

        val htmlString = when (tooltipType) {
            TooltipType.LABEL -> """
                        <b>Ярлык:</b><br>
                        Короткий тап - открывает/закрывает панель с устройствами.<br>
                        Нажать, переместить влево - перемещать ярлык.<br>
                        Свайп по ярлыку вправо - задвинет его.<br><br>
                        <b>Кнопка назад:</b><br>
                        если активирован - системная кнопка или жест назад, перед закрытием приложения развернет панель.<br>
                        если ярлык выключен - эта настройка активна всегда.<br><br>
                        <b>Двойной тап:</b><br>
                        если активирован - двойной тап по ярлыку обновить страницу.<br>
                        если выключен - двойной тап откроет панель на половину.
                    """.trimIndent()

            TooltipType.CHECK_NEW_VERSION -> """
                        <b>Проверять версию при старте:</b><br>
                        Будет проверять новую версию приложения при каждом старте.<br>
                        Нужно разрешение на показ уведомлений (будет запрошено при первом обнаружении новой версии)
                    """.trimIndent()
            TooltipType.SPLIT -> """
                        <b>Режим разделения:</b><br>
                        Будет определять текущую сеть, и автоматически переключаться на нее. Обновить - потянуть шторку чуть вниз и отпустить.<br><br>
                        Определяет сеть по имени (ssid), мак-адресу роутера(только первые 5 октетов) и по подсети.<br><br>
                       
                        Для работы этой функции нужны разрешение на геолокацию и включенная геолокация. *<br><br>
                       
                        Если у вас меш-сеть, (имя и подсеть должны совпадать) нужно добавить каждую точку к первой найденной (RSM покажет диалог объединения)<br>
                        Определяет wifi сети и отдельно сотовую.<br>
                        Можно создать сети вручную, для организации списков устройств.<br><br>
                       
                        
                        <small><i>* Доступ к геолокации нужен только для определения имени сети (ssid) и мак-адреса роутера.<br>
                        RSM не получает данные о местоположении.</i></small>
                        
                     """.trimIndent()
            TooltipType.NOT_GEO -> """
                        <b>Не сообщать о выключенной геолокации</b><br>
                        Не будет проверять включена ли геолокация.<br>
                        И не будет показывать диалог отправляющий в настройки.<br><br>
                        Если геолокация будет выключена, RSM не сможет получить данные сети и определить ее, и вместо устройств текущей сети покажет все устройства.
                        
                    """.trimIndent()
            TooltipType.SPLIT_MANUAL -> """
                        <b>Ручное разделение</b><br>
                        Просто создавать списки устройств, вручную переключаясь между ними.<br><br>
                        Этот режим не требует ни разрешений на доступ к местоположению, ни включенной геолокации.<br>
                        Но и переключать сети нужно самостоятельно.
                    """.trimIndent()
            TooltipType.NETWORKS -> """
                        <b>Найденные сети:</b><br>
                        <b>default</b> - сеть созданная по умолчанию, для режима с выключенным разделением.<br>
                        <b>Сотовая</b> (cellular) - Сотовая сеть, для устройств доступных из интернета, сюда устройства добавляются вручную.<br>
                        <b>"Имя сети"</b> - созданные автоматически wifi сети и сети созданные вручную.<br>
                        <br>
                        <b>Настройки:</b><br>
                        <b>Имя</b> - пользовательское имя, можно изменить.<br>
                        <b>SSID</b> - имя сети, участвует в определении сети, если изменить RSM не сможет корректно определить эту сеть.<br>
                        <b>Mac-префикс</b> - первые 5 октетов мак-адреса (bssid), то же не стоит изменять как и ssid.<br>
                        <b>Подсеть</b> - подсеть сети, как и SSID и Mac-префикс участвует в определении сети, изменять не стоит.<br>
                        <b>Не проверять сертификаты</b> - если выбрано 'не проверять', то при сканировании сети не будет проверять https сертификаты, доверяя им всем. 
                        Потенциально не безопасно, но может пригодиться если есть самоподписанные сертификаты в сети. 
                        Действует только при сканировании локальной сети.<br>
                        <b>Mac-префиксы дополнительных точек</b> - если у вас меш сеть, сюда записываются все мак-префиксы точек, которые вы добавили.<br>
                        <br>
                        <b>Объединить</b> - добавить сеть как меш точку, это удалит сеть с которой объединяете, устройства переместятся в редактируемую.
                    """.trimIndent()
            TooltipType.PING -> """
                        <b>Пинговать устройства</b><br>
                        При обновлении шторки или списка устройств в "Устройствах" (потянуть чуть вниз и отпустить) будет пробегать по всем устройствам и проверять отвечает ли они на пинг.<br>
                        Если устройство ответило - появится галочка справа от имени.<br><br>
                        <b>Отмечать неактивные устройства крестиком</b><br>
                        устройства не ответившие на пинг будут помечаться крестиком.
                        
                    """.trimIndent()
            TooltipType.HISTORY -> """
                        <b>Вести историю открытых устройств</b><br>
                        Будет запоминать все открытые устройства и возвращаться на них по кнопке назад.
                    """.trimIndent()
            TooltipType.SWIPE_REFRESH -> """
                        <b>Обновление страницы свайпом</b><br>
                        Жест - 'свайп вниз' будет обновлять страницу.<br> 
                        Отключается автоматически на зуммированных страницах, с прокрученным вниз контентом итд.<br> <br> 
                        Эта настройка отключает обновление свайпом глобально для всех устройств. <br> 
                        Если на вашем устройстве работает некорректно - можно отключить только для него в настройках устройства. 
                    """.trimIndent()
            TooltipType.SWIPE_OPEN_PANEL -> """
                        <b>Открытие панели широким свайпом</b><br>
                        Жест - 'свайп вверх' будет открывать панель.<br> 
                        Жест должен быть достаточно энергичным и продолжительным
                        
                    """.trimIndent()
            TooltipType.HEADER_BLUR -> """
                        <b>Прозрачный системный статус бар</b><br>
                        Сделает шапку под статус баром прозрачной. <br>
                        К сожалению, нативно и универсально сделать прозрачным статус бар и чтобы он не перекрывал контент - невозможно.<br>
                        Поэтому на некоторых сайтах это может ломать верстку.<br>
                        <br> 
                        Эта настройка отключит прозрачность глобально.<br> 
                        Можно отключить для каждого устройства отдельно в настройках устройства.
                    """.trimIndent()
        }

        return Html.fromHtml(htmlString, Html.FROM_HTML_MODE_LEGACY, imageGetter, null)
    }
    private fun createConfiguredBalloon(spannedText: Spanned): Balloon {
        return Balloon.Builder(this)
            .setText(spannedText)
            .setArrowSize(12)

            .setArrowColor(getColorFromAttr(R.attr.buttonStrokeTint))
            .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
            .setArrowPosition(0.5f)
            .setTextGravity(Gravity.START)
            .setWidth(BalloonSizeSpec.WRAP)
            .setHeight(BalloonSizeSpec.WRAP)
            .setTextSize(15f)
            .setCornerRadius(8f)

            .setPadding(12)
            .setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.spinner_dropdown_background))
            .setBalloonAnimation(BalloonAnimation.FADE)
            .setDismissWhenClicked(true)
            .setLifecycleOwner(this)
            .build()
    }
    fun Context.getColorFromAttr(@AttrRes attrColor: Int): Int {
        val typedValue = TypedValue()
        theme.resolveAttribute(attrColor, typedValue, true)
        return typedValue.data
    }

    private enum class TooltipType {
        LABEL,
        CHECK_NEW_VERSION,
        SPLIT,
        NOT_GEO,
        SPLIT_MANUAL,
        NETWORKS,
        PING,
        HISTORY,
        SWIPE_REFRESH,
        HEADER_BLUR,
        SWIPE_OPEN_PANEL

    }


}