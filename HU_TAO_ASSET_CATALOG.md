# Hu Tao keyboard asset catalog

Source: the untouched `baidu_theme.bda` extracted from the OnePlus Ace Pro Hu Tao
Edition firmware.

## Totals

- 533 files in the archive (excluding four directory entries)
- 526 PNG images: 450 portrait resources, 75 landscape resources, and `demo.png`
- One font: `res/jingang.ttf`
- Four Baidu theme configuration files
- `Info.txt` and `Token.txt`
- 50 original Hu Tao PNGs currently copied into Hu Tao Board

Suffix `_1` is normally the idle state and `_2` the pressed state. `h_` images
are smaller portrait variants of the corresponding `res_` foreground glyphs.

## Top-level and configuration files

- `demo.png`
- `Info.txt`
- `Token.txt`
- `port/animationConfig`
- `port/appearanceConfig`
- `land/animationConfig`
- `land/appearanceConfig`

## Typeface

- `res/jingang.ttf` — the original keyboard typeface (about 9 MB)

## Portrait resources (`res/`)

### Backgrounds and panels

- `res_26back1_1`
- `res_9back1_1`
- `res_hwback1_1`
- `land_res_26back1_1–2`
- `land_res_9back1_1`
- `land_res_hwback1_1`
- `res_cand1_1`, `land_res_cand1_1`, `cand_1–2`
- `res_hint_1`, `res_hint_3`, `res_list_1`

### Press animation

- `hudie00–29` — all 30 butterfly animation frames

### 26-key button backgrounds

- `res_26btn_d_1–2`, `res_26btn_p_1–2`, `res_26btn_q_1–2`,
  `res_26btn_v_1–2`, `res_26btn_zm_1–2` — regular framed keys
- `res_26btn_yc_1–2`, `res_26btn_zc_1–2` — narrow framed variants
- `res_26space_1–2` — ornate wide spacebar
- `res_26btn_F1_1–2` — clean circular utility button
- `res_26enter_1–2` — ornate circular Enter button with butterflies
- `res_26btn_del_1–2` — diamond Delete button with the geometric emblem
- `res_26btn_shift_1–2` — diamond Shift button with the Pyro emblem
- `res_26btn_gn_1–2`, `res_26btn_zy_1–2` — diamond function variants

### 9-key button backgrounds

- `res_9btn_0_1–2`
- `res_9btn_F1_1–2`
- `res_9btn_del_1–2`
- `res_9btn_gn_1–2`
- `res_9btn_hc_1–2`
- `res_9btn_u_1–2`
- `res_9btn_zm_1–2`
- `res_9btn_zy_1–2`
- `res_9space_1–2`

### Handwriting button backgrounds

- `hw_btn_1–2`
- `hw_space_1–2`
- `hw_zy_1–2`

### Letter glyphs

- `res_en26_1–26`, `h_en26_1–26`
- `res_en26s_1–26`, `h_en26s_1–26`
- `res_en9_1–12`, `h_en9_1–12`

### Number glyphs

- `res_num26_1–19`, `res_num26_24–26`, `res_num26_31–34`
- `h_num26_1–19`, `h_num26_24–26`, `h_num26_31–34`
- `res_num9_1–13`, `h_num9_1–13`

### Enter action labels

- `res_enter_1–8`, `h_enter_1–8`
- Meanings: Next item, Go, Send, Confirm, Search, Clear, Done, and Re-enter

### Functional foreground glyphs

- `res_fore2_2–6`, `res_fore2_9–10`, `res_fore2_12–16`,
  `res_fore2_19`, `res_fore2_21`, `res_fore2_23`, `res_fore2_26–30`,
  `res_fore2_33–34`, `res_fore2_36–38`, `res_fore2_41`,
  `res_fore2_43–44`
- `h_fore2_3–4`, `h_fore2_6`, `h_fore2_9–10`, `h_fore2_13–16`,
  `h_fore2_19`, `h_fore2_21`, `h_fore2_23`, `h_fore2_26–30`,
  `h_fore2_33`, `h_fore2_41`, `h_fore2_43–44`
- `res_fore3_1–7`, `h_fore3_2–3`
- `res_fore5_1`, `res_fore5_5`, `h_fore5_1`, `h_fore5_5`
- `res_fore_voice_3–5`, `res_fore_voice_7`, `res_fore_voice_9–10`
- `h_fore_voice_3–5`, `h_fore_voice_7`, `h_fore_voice_9–10`

These include Shift arrows, left/right/down arrows, `123`, alphabet mode,
Chinese/English toggles, symbol mode, word-segmentation controls, microphone,
the curved return arrow, and several Baidu-specific mode controls. The curved
arrow in the reference screenshots is `res_fore_voice_7` (also duplicated as
`res_fore2_5` and `res_fore2_38`). There is no separate conventional Backspace
foreground; its geometric symbol is baked into `res_26btn_del`.

### Punctuation, handwriting, symbol, and hint glyphs

- `res_bh_1–5`, `res_bh_8–9`; `h_bh_1–5`, `h_bh_8–9`
- `res_bt_13`, `h_bt_13`
- `res_hw_1–5`, `h_hw_1–5`
- `res_padfuhao_1–4`, `h_padfuhao_1–4`
- `res_sym_num_1–3`, `res_sym_num_5–10`
- `h_sym_num_1–3`, `h_sym_num_6–8`, `h_sym_num_10`
- `res_tip_3–6`, `h_tip_3–6`

## Landscape resources (`land/res/`)

- `h_fore5_1`, `h_num9_11`
- `res_26btn_F1_1–2`, `res_26btn_del_1–2`, `res_26btn_gn_1–2`,
  `res_26btn_shift_1–2`, `res_26btn_zy_1–2`, `res_26enter_1–2`,
  `res_26space_1–2`
- `res_9btn_0_1–2`, `res_9btn_F1_1–2`, `res_9btn_del_1–2`,
  `res_9btn_gn_1–2`, `res_9btn_hc_1–2`, `res_9btn_zy_1–2`,
  `res_9space_1–2`
- `res_enter_1–8`
- `res_fore2_2–3`, `res_fore2_5–6`, `res_fore2_9`, `res_fore2_12–13`,
  `res_fore2_21`, `res_fore2_23`, `res_fore2_26–29`, `res_fore2_34`,
  `res_fore2_38`, `res_fore2_41`, `res_fore2_43–44`
- `res_fore3_4`
- `res_fore5_1`, `res_fore5_5`
- `res_fore_voice_3–4`, `res_fore_voice_7`, `res_fore_voice_10`
- `res_hw_F1_1–2`, `res_hwf_gn_1–2`
- `res_num26btn_1–2`, `res_num9_11`
- `res_symbol_1–2`
- `res_sym_num_5`, `res_sym_num_9–10`

## Original images currently bundled in Hu Tao Board

- `hu_tao_keyboard_background`
- `hu_tao_key_normal`, `hu_tao_key_pressed`
- `hu_tao_space_normal`, `hu_tao_space_pressed`
- `hu_tao_shift_normal`, `hu_tao_shift_pressed`
- `hu_tao_delete_normal`, `hu_tao_delete_pressed`
- `hu_tao_enter_normal`, `hu_tao_enter_pressed`
- `hu_tao_round_function_normal`, `hu_tao_round_function_pressed`
- `hu_tao_diamond_function_normal`, `hu_tao_diamond_function_pressed`
- `hu_tao_butterfly_00–29`
- `hu_tao_return_arrow`
- `hu_tao_toolbar_background`
- `hu_tao_toolbar_backpack`, `hu_tao_toolbar_backpack_pressed`
- `hu_tao_toolbar_flourish`

The toolbar files are exact copies of `res_cand1_1`, `res_fore3_1`,
`res_fore3_5`, and `res_fore2_12`. The fork also contains the new
`hu_tao_backspace_filled` vector and four toolbar vectors reproducing the
keyboard, cursor, search, and emoji glyphs visible in the original UI.
