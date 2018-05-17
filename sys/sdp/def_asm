; Printer definitions for screen dump  V2.02    1985  Tony Tebby  QJUMP
 
        section prdefs
        xdef    dm_prt1
        xdef    dm_prt2
        xdef    dm_prt3
        xdef    dm_prt4
        xdef    dm_prt5
        xdef    dm_prt6
        xdef    dm_prt7
        xdef    dm_prt8
        xdef    dm_prt9
        xdef    dm_prt10
        xdef    dm_prt11
        xdef    dm_prt12
        xdef    dm_prt13
        xdef    dm_prt14
        xdef    dm_prt15
        xdef    dm_prt16
        xdef    dm_prt17
        xdef    dm_prt18
        xdef    dm_prt19
        xdef    dm_prt20
;**        xdef    dm_prt21
;**        xdef    dm_prt22
;**        xdef    dm_prt23
;**        xdef    dm_prt24
;**        xdef    dm_prt25

        include dev8_sys_sdp_data
esc     equ     $1b
*
* Old Epson type
*
dm_prt1
        dc.w    pt_norm-*
        dc.b    dpc.pver,0,8,7,-1,0
        dc.w    ctl_1a-*,ctl_1b-*-2,p_1x1-*-4,p_1x1-*-6
        dc.w    ctl_1b-*,ctl_1b-*-2,p_1x2-*-4,p_2x2-*-6
        dc.w    ctl_1a-*,ctl_1a-*-2,p_2h2-*-4,p_4x2-*-6
*
* Epson FX80 and compatibles with CRT mode graphics
*
dm_prt2
        dc.w    pt_norm-*
        dc.b    dpc.pver,0,8,7,-1,0
        dc.w    ctl_2a-*,ctl_2b-*-2,p_1x1-*-4,p_1x1-*-6
        dc.w    ctl_2a-*,ctl_2a-*-2,p_1x1-*-4,p_2x1-*-6
        dc.w    ctl_2a-*,ctl_2a-*-2,p_2h2-*-4,p_4x2-*-6
*
* Seikosha GP-100A
*
dm_prt3
        dc.w    pt_norm-*
        dc.b    dpc.pver,$80,7,0,1,0
        dc.w    ctl_3-*,ctl_3-*-2,p_1x1-*-4,p_1x1-*-6
        dc.w    ctl_3-*,ctl_3-*-2,p_1x2-*-4,p_2x2-*-6
        dc.w    ctl_3-*,ctl_3-*-2,p_1x2-*-4,p_2x2-*-6
*
* Seikosha GP-250X
*
dm_prt4
        dc.w    pt_norm-*
        dc.b    dpc.pver,0,8,0,1,0
        dc.w    ctl_4-*,ctl_4-*-2,p_1x1-*-4,p_1x1-*-6
        dc.w    ctl_4-*,ctl_4-*-2,p_1x2-*-4,p_2x2-*-6
        dc.w    ctl_4-*,ctl_4-*-2,p_1x2-*-4,p_2x2-*-6
*
* Seikosha GP-700A
*
dm_prt5
        dc.w    cl_norm-*
        dc.b    dpc.pver,0,8,0,1,0
        dc.w    ctl_5-*,ctl_5-*-2,p_1x1-*-4,p_1x1-*-6
        dc.w    ctl_5-*,ctl_5-*-2,p_1x2-*-4,p_2x2-*-6
        dc.w    ctl_5-*,ctl_5-*-2,p_1x2-*-4,p_3x2-*-6
*
* Epson JX80
*
dm_prt6
        dc.w    cl_norm-*
        dc.b    dpc.pver,0,8,7,-1,0
        dc.w    ctl_6a-*,ctl_6b-*-2,p_1x1-*-4,p_1x1-*-6
        dc.w    ctl_6a-*,ctl_6a-*-2,p_1x1-*-4,p_2x1-*-6
        dc.w    ctl_6a-*,ctl_6a-*-2,p_2h2-*-4,p_4x2-*-6
*
* Canon PJ1080A
*
dm_prt7
        dc.w    cl_norm-*
        dc.b    dpc.phor,0,8,7,-1,0
        dc.w    ctl_7-*,ctl_7-*-2,p_1x1-*-4,p_1x1-*-6
        dc.w    ctl_7-*,ctl_7-*-2,p_1x2-*-4,p_2x2-*-6
        dc.w    ctl_7-*,ctl_7-*-2,p_1x2-*-4,p_3x2-*-6
*
* Brother HR4
*
dm_prt8
        dc.w    pt_norm-*
        dc.b    dpc.pver,0,8,7,-1,0
        dc.w    ctl_8a-*,ctl_8b-*-2,p_1x1-*-4,p_1x1-*-6
        dc.w    ctl_8b-*,ctl_8b-*-2,p_1x2-*-4,p_2x2-*-6
        dc.w    ctl_8a-*,ctl_8a-*-2,p_2h2-*-4,p_4x2-*-6
*
* Olivetti JP101
*
dm_prt9
        dc.w    pt_norm-*
        dc.b    dpc.phor,0,8,7,-1,0
        dc.w    ctl_9a-*,ctl_9b-*-2,p_1x1-*-4,p_1x1-*-6
        dc.w    ctl_9b-*,ctl_9b-*-2,p_1x2-*-4,p_3x2-*-6
        dc.w    ctl_9a-*,ctl_9a-*-2,p_2h2-*-4,p_4x2-*-6
*
* Epson LQ2500 8 pin / mono
*
dm_prt10
        dc.w    pt_norm-*
        dc.b    dpc.pver,0,8,7,-1,0
        dc.w    ctl_10b-*,ctl_10a-*-2,p_1x1-*-4,p_1x1-*-6
        dc.w    ctl_10c-*,ctl_10b-*-2,p_2x1-*-4,p_2x1-*-6
        dc.w    ctl_10b-*,ctl_10b-*-2,p_2h2-*-4,p_4x2-*-6
*
* Epson LQ2500 8 pin / colour
*
dm_prt11
        dc.w    cl_norm-*
        dc.b    dpc.pver,0,8,7,-1,0
        dc.w    ctl_11b-*,ctl_11a-*-2,p_1x1-*-4,p_1x1-*-6
        dc.w    ctl_11c-*,ctl_11b-*-2,p_2x1-*-4,p_2x1-*-6
        dc.w    ctl_11b-*,ctl_11b-*-2,p_2h2-*-4,p_4x2-*-6
*
* Epson LQ2500 24 pin
*
dm_prt12
        dc.w    pt_norm-*
        dc.b    dpc.pver,0,24,31,-1,-1
        dc.w    ctl_12a-*,ctl_12a-*-2,p_1x2-*-4,p_1x1-*-6
        dc.w    ctl_12b-*,ctl_12b-*-2,p_2x3-*-4,p_3x2-*-6
        dc.w    ctl_12b-*,ctl_12b-*-2,p_3x4-*-4,p_6x4-*-6
*
* Epson LQ2500 24 pin / colour
*
dm_prt13
        dc.w    cl_norm-*
        dc.b    dpc.pver,0,24,31,-1,-1
        dc.w    ctl_13a-*,ctl_13a-*-2,p_1x2-*-4,p_1x1-*-6
        dc.w    ctl_13b-*,ctl_13b-*-2,p_2x3-*-4,p_3x2-*-6
        dc.w    ctl_13b-*,ctl_13b-*-2,p_3x4-*-4,p_6x4-*-6
*
* Tandy DMP 105
*
dm_prt14
        dc.w    pt_norm-*
        dc.b    dpc.pver,$80,7,0,1,0
        dc.w    ctl_14b-*,ctl_14a-*-2,p_1x1-*-4,p_1x1-*-6
        dc.w    ctl_14a-*,ctl_14b-*-2,p_1x2-*-4,p_2x1-*-6
        dc.w    ctl_14b-*,ctl_14b-*-2,p_2h2-*-4,p_4x2-*-6
*
* Centronics 739
*
dm_prt15
        dc.w    pt_norm-*
        dc.b    dpc.pver,$20,6,0,1,0
        dc.w    ctl_15a-*,ctl_15a-*-2,p_1x1-*-4,p_1x1-*-6
        dc.w    ctl_15a-*,ctl_15a-*-2,p_1x1-*-4,p_2x1-*-6
        dc.w    ctl_15a-*,ctl_15a-*-2,p_2h2-*-4,p_3x2-*-6
*
* C.Itoh 7500
*
dm_prt16
        dc.w    pt_norm-*
        dc.b    dpc.pver,0,8,0,1,0
        dc.w    ctl_16b-*,ctl_16a-*-2,p_1x1-*-4,p_1x1-*-6
        dc.w    ctl_16c-*,ctl_16b-*-2,p_2x1-*-4,p_2x1-*-6
        dc.w    ctl_16b-*,ctl_16b-*-2,p_2h2-*-4,p_4x2-*-6
*
* Toshiba TH-2100H
*
dm_prt17
        dc.w    pt_norm-*
        dc.b    dpc.pver,0,24,31,-1,-1
        dc.w    ctl_17a-*,ctl_17a-*-2,p_1x2-*-4,p_2x2-*-6
        dc.w    ctl_17a-*,ctl_17a-*-2,p_2x3-*-4,p_3x2-*-6
        dc.w    ctl_17a-*,ctl_17a-*-2,p_3x4-*-4,p_6x4-*-6
*
* 8056 
*
dm_prt18
        dc.w    pt_norm-*
        dc.b    dpc.pver,0,8,7,-1,0
        dc.w    ctl_18a-*,ctl_18a-*-2,p_1x1-*-4,p_1x1-*-6
        dc.w    ctl_18a-*,ctl_18a-*-2,p_1x1-*-4,p_2x1-*-6
        dc.w    ctl_18a-*,ctl_18a-*-2,p_2h2-*-4,p_3x2-*-6

*
* Epson MX100 wide carriage
*
dm_prt19
        dc.w    pt_norm-*
        dc.b    dpc.pver,0,8,7,-1,0
        dc.w    ctl_19a-*,ctl_19b-*-2,p_1x1-*-4,p_1x1-*-6
        dc.w    ctl_19b-*,ctl_19b-*-2,p_1x2-*-4,p_2x2-*-6
        dc.w    ctl_19a-*,ctl_19a-*-2,p_2h2-*-4,p_4x2-*-6
*
* Epson FX100 wide carriage
*
dm_prt20
        dc.w    pt_norm-*
        dc.b    dpc.pver,0,8,7,-1,0
        dc.w    ctl_20a-*,ctl_20b-*-2,p_1x1-*-4,p_1x1-*-6
        dc.w    ctl_20a-*,ctl_20a-*-2,p_1x1-*-4,p_2x1-*-6
        dc.w    ctl_20a-*,ctl_20a-*-2,p_2h2-*-4,p_4x2-*-6
*
* OKI Microline 82A with OK writer
*
;**dm_prt21
;**        dc.w    pt_norm-*
;**        dc.b    dpc.pves,3,7,0,1,0
;**        dc.w    ctl_21a-*,ctl_21b-*-2,p_1x1-*-4,p_1x1-*-6
;**        dc.w    ctl_21a-*,ctl_21a-*-2,p_1x1-*-4,p_2x1-*-6
;**        dc.w    ctl_21a-*,ctl_21a-*-2,p_2h2-*-4,p_4x2-*-6
*
* Fasttext 80
*
dm_prt22
;**        dc.w    pt_norm-*
;**        dc.b    dpc.pver,0,8,7,-1,0
;**        dc.w    ctl_22a-*,ctl_22b-*-2,p_1x1-*-4,p_1x1-*-6
;**        dc.w    ctl_22b-*,ctl_22b-*-2,p_1x2-*-4,p_2x2-*-6
;**        dc.w    ctl_22a-*,ctl_22a-*-2,p_2x3-*-4,p_3x2-*-6
*
* MT-80
*
dm_prt23
;**        dc.w    pt_norm-*
;**        dc.b    dpc.pver,0,8,7,-1,0
;**        dc.w    ctl_23b-*,ctl_23b-*-2,p_1x1-*-4,p_1x1-*-6
;**        dc.w    ctl_23a-*,ctl_23a-*-2,p_2x1-*-4,p_3x1-*-6
;**        dc.w    ctl_23a-*,ctl_23a-*-2,p_3x2-*-4,p_6x2-*-6
*
* OKI 192
*
dm_prt24
;**        dc.w    pt_norm-*
;**        dc.b    dpc.pves,3,7,0,1,0
;**        dc.w    ctl_24a-*,ctl_24b-*-2,p_1x1-*-4,p_1x1-*-6
;**        dc.w    ctl_24c-*,ctl_24c-*-2,p_2x1-*-4,p_3x1-*-6
;**        dc.w    ctl_24c-*,ctl_24c-*-2,p_3x2-*-4,p_6x2-*-6
*
* OKI 193
*
dm_prt25
;**        dc.w    pt_norm-*
;**        dc.b    dpc.pves,3,7,0,1,0
;**        dc.w    ctl_25a-*,ctl_25b-*-2,p_1x1-*-4,p_1x1-*-6
;**        dc.w    ctl_25c-*,ctl_25c-*-2,p_2x1-*-4,p_3x1-*-6
;**        dc.w    ctl_25c-*,ctl_25c-*-2,p_3x2-*-4,p_6x2-*-6
        page
*
* shading/colour maps
*
pt_norm dc.b    0,0,2,2,5,5,7,7         normal shades mode 4
        dc.b    7,7,5,5,2,2,0,0         inverse shades
        dc.b    0,1,2,3,4,5,6,7         normal shades mode 8
        dc.b    7,6,5,4,3,2,1,0         inverse shades
*
sh_norm dc.b    255,255,150,150,76,76,0,0  random mode 4
        dc.b    0,0,76,76,150,150,255,255
        dc.b    255,198,150,108,76,32,16,0 random mode 8
        dc.b    0,16,32,76,108,150,198,255
*
cl_norm dc.b    0,0,2,2,4,4,7,7         mode 4 colours
        dc.b    7,7,2,2,4,4,0,0         ... black and white swapped
        dc.b    0,1,2,3,4,5,6,7         normal colours
        dc.b    7,1,2,3,4,5,6,0         swap black and white
*
        page
*
* pattern definitions
*
p_1x1   dc.w    1,1             1x1 both modes
        dc.b    %00001111
*
p_1x2   dc.w    1,2             1x2 512 mode only
        dc.b    %00001111
        dc.b    %00110011
*
;**p_3x1   dc.w    3,1             3x1
;**        dc.b    %00110011,%00001111,%00110011
;***
p_2x1   dc.w    2,1             2x1
        dc.b    %00001111,%00110011
*
p_2x2   dc.w    2,2             2x2 256 mode only
        dc.b    %00011011,%00110101
        dc.b    %01000111,%00101011
*
p_2h2   dc.w    2,2             2x2 512 mode only
        dc.b    %00000011,%00111111
        dc.b    %00001111,%00000011
*
p_3x2   dc.w    3,2             3x2
        dc.b    %00100101,%00001011,%00000111
        dc.b    %00011011,%01010111,%00111001
*
p_4x2   dc.w    4,2             4x2
        dc.b    %00000011,%00011011,%00000101,%00001111
        dc.b    %00001111,%00100101,%01011011,%00110111
*
;**p_6x2   dc.w    6,2             6x2
;**        dc.b    %00001011,%00000111,%00010001,%00000111,%00111001,%00000111
;**        dc.b    %00000111,%00011001,%00100111,%01001011,%00000111,%00000001
;***
p_2x3   dc.w    2,3             2x4
        dc.b    %00100011,%00011111
        dc.b    %00001101,%01010111
        dc.b    %00000111,%00111011
*
p_2x4   dc.w    2,4             2x4
        dc.b    %00000011,%00001101
        dc.b    %00011011,%00100111
        dc.b    %00000101,%01011011
        dc.b    %00001111,%00110111
*
p_3x4   dc.w    3,4             3x4
        dc.b    %00001001,%00000111,%00000011
        dc.b    %00110111,%01000011,%00011101
        dc.b    %00001001,%00000111,%00100011
        dc.b    %00000111,%00010011,%00001101
*
p_4x4   dc.w    4,4             4x4
        dc.b    %00000111,%00010001,%00001111,%00000011
        dc.b    %00001011,%01100111,%00001011,%00010101
        dc.b    %00010101,%00001011,%00100111,%00001011
        dc.b    %00000011,%00001111,%00010001,%00000111
*
p_6x4   dc.w    6,4             6x4
        dc.b    %00000011,%00000111,%00000001,%00000111,%00011001,%00000111
        dc.b    %00000111,%00011001,%00100111,%01001011,%00000111,%00000001
        dc.b    %00001011,%00000111,%00000011,%00000111,%00001011,%00010111
        dc.b    %00000101,%00000011,%00010101,%00101011,%00000101,%00001011
        page
*
* printer 1/2/6/19/20 (FX-80) 22 (Fasttext)
*
ctl_1a  dc.w    80,1,120,72
        dc.w    0
        dc.w    set_ep9-*,0,0,pre_epl-*-6
        dc.w    0,0,epi_nl-*-4,res_ep6-*-6
ctl_1b  dc.w    80,1,60,72
        dc.w    0
        dc.w    set_ep9-*,0,0,pre_epk-*-6
        dc.w    0,0,epi_nl-*-4,res_ep6-*-6
*
ctl_2a  dc.w    80,1,90,72
        dc.w    0
        dc.w    set_ep9-*,0,0,pre_epc-*-6
        dc.w    0,0,epi_nl-*-4,res_ep6-*-6
ctl_2b  equ     ctl_1b

ctl_19a dc.w    136,1,120,72
        dc.w    0
        dc.w    set_ep9-*,0,0,pre_epl-*-6
        dc.w    0,0,epi_nl-*-4,res_ep6-*-6
ctl_19b dc.w    136,1,60,72
        dc.w    0
        dc.w    set_ep9-*,0,0,pre_epk-*-6
        dc.w    0,0,epi_nl-*-4,res_ep6-*-6
*
ctl_20a dc.w    136,1,90,72
        dc.w    0
        dc.w    set_ep9-*,0,0,pre_epc-*-6
        dc.w    0,0,epi_nl-*-4,res_ep6-*-6
ctl_20b equ     ctl_19b
*
;**ctl_22a dc.w    80,1,72,72
;**        dc.w    0
;**        dc.w    set_ep9-*,0,0,pre_epp-*-6
;**        dc.w    0,0,epi_nl-*-4,res_ep6-*-6
;**ctl_22b dc.w    80,1,60,72
;**        dc.w    0
;**        dc.w    set_ep9-*,0,0,pre_eps-*-6
;**        dc.w    0,0,epi_nl-*-4,res_ep6-*-6
*
* printer 3 (GP-100A)
*
ctl_3   dc.w    80,1,60,63
        dc.w    0
        dc.w    set_gp1-*,0,0,0
        dc.w    0,0,epi_nl-*-4,res_gp1-*-6
*
* printer 4 (GP-250X)
*
ctl_4   dc.w    80,1,60,72
        dc.w    0
        dc.w    set_gp2-*,0,0,pre_gp2-*-6
        dc.w    0,0,epi_nl-*-4,res_gp2-*-6
*
* printer 5 (GP-700A)
*
ctl_5   dc.w    80,1,80,80
        dc.w    ctl_5_c-*
        dc.w    set_gp7-*,0,prc_gp7-*-4,0
        dc.w    0,0,epi_nl-*-4,0
ctl_5_c dc.w    4
        dc.b    6,%01010100,1,%00001110,5,%00110010,0,%00000001
*
* printer 6 (JX80)
*
ctl_6a  dc.w    80,1,90,72
        dc.w    ctl_6_c-*
        dc.w    set_ep9-*,0,prc_ep-*-4,pre_epc-*-6
        dc.w    epi_cr-*,0,epi_nl-*-4,res_ep6-*-6
ctl_6b  dc.w    80,1,60,72
        dc.w    ctl_6_c-*
        dc.w    set_ep9-*,0,prc_ep-*-4,pre_epk-*-6
        dc.w    epi_cr-*,0,epi_nl-*-4,res_ep6-*-6
ctl_6_c dc.w    4
        dc.b    4,%01010100,1,%00001110,2,%00110010,0,%00000001
*
* printer 7 (PJ1080A)
*
ctl_7   dc.w    80,1,80,80
        dc.w    ctl_7_c-*
        dc.w    0,pre_pjc-*-2,0,0
        dc.w    0,0,0,res_pjc-*-6
ctl_7_c dc.w    3
        dc.b    0,%11001100,0,%11110000,0,%10101010
*
* printer 8 (HR4)
*
ctl_8a  dc.w    80,1,120,72
        dc.w    0
        dc.w    set_hr4-*,0,0,pre_epl-*-6
        dc.w    0,0,epi_nl-*-4,res_ep6-*-6
ctl_8b  dc.w    80,1,60,72
        dc.w    0
        dc.w    set_hr4-*,0,0,pre_epk-*-6
        dc.w    0,0,epi_nl-*-4,res_ep6-*-6
*
* printer 9 (JP101)
*
ctl_9a  dc.w    80,1,110,72
        dc.w    0
        dc.w    set_ol3-*,0,0,0
        dc.w    0,0,0,epi_pad-*-6
ctl_9b  dc.w    80,1,110,108
        dc.w    0
        dc.w    set_ol2-*,0,0,0
        dc.w    0,0,0,epi_pad-*-6
*
* printer 10 (LQ2500 8 pin mono)
*
ctl_10a dc.w    136,1,60,60
        dc.w    0
        dc.w    set_ep9-*,0,0,pre_epk-*-6
        dc.w    0,0,epi_nl-*-4,res_ep6-*-6
ctl_10b dc.w    136,1,80,60
        dc.w    0
        dc.w    set_ep9-*,0,0,pre_epb-*-6
        dc.w    0,0,epi_nl-*-4,res_ep6-*-6
ctl_10c dc.w    136,1,120,60
        dc.w    0
        dc.w    set_ep9-*,0,0,pre_epl-*-6
        dc.w    0,0,epi_nl-*-4,res_ep6-*-6
*
* printer 11 (LQ2500 8 pin colour)
*
ctl_11a dc.w    136,1,60,60
        dc.w    ctl_11_c-*
        dc.w    set_ep9-*,0,prc_ep-*-4,pre_epk-*-6
        dc.w    epi_cr-*,0,epi_nl-*-4,res_ep6-*-6
ctl_11b dc.w    136,1,80,60
        dc.w    ctl_11_c-*
        dc.w    set_ep9-*,0,prc_ep-*-4,pre_epb-*-6
        dc.w    epi_cr-*,0,epi_nl-*-4,res_ep6-*-6
ctl_11c dc.w    136,1,120,60
        dc.w    ctl_11_c-*
        dc.w    set_ep9-*,0,prc_ep-*-4,pre_epl-*-6
        dc.w    epi_cr-*,0,epi_nl-*-4,res_ep6-*-6
ctl_11_c dc.w   4
        dc.b    4,%01010100,1,%00001110,2,%00110010,0,%00000001
*
* printer 12 (LQ2500 24 pin mono)
*
ctl_12a dc.w    136,1,120,180
        dc.w    0
        dc.w    set_ep9-*,0,0,pre_epd-*-6
        dc.w    0,0,epi_nl-*-4,res_ep6-*-6
ctl_12b dc.w    136,1,180,180
        dc.w    0
        dc.w    set_ep9-*,0,0,pre_ept-*-6
        dc.w    0,0,epi_nl-*-4,res_ep6-*-6
*
* printer 13 (LQ2500 24 pin colour)
*
ctl_13a dc.w    136,1,120,180
        dc.w    ctl_13_c-*
        dc.w    set_ep9-*,0,prc_ep-*-4,pre_epd-*-6
        dc.w    epi_cr-*,0,epi_nl-*-4,res_ep6-*-6
ctl_13b dc.w    136,1,180,180
        dc.w    ctl_13_c-*
        dc.w    set_ep9-*,0,prc_ep-*-4,pre_ept-*-6
        dc.w    epi_cr-*,0,epi_nl-*-4,res_ep6-*-6
ctl_13_c equ    ctl_11_c
*
* Printer 14 (Tandy DMP-105)
*
ctl_14a dc.w    80,1,60,72
        dc.w    0
        dc.w    set_tyn-*,0,0,pre_ty1-*-6
        dc.w    0,0,epi_cr-*-4,res_ty-*-6
ctl_14b dc.w    80,1,100,72
        dc.w    0
        dc.w    set_tyc-*,0,0,pre_ty1-*-6
        dc.w    0,0,epi_cr-*-4,res_ty-*-6
*
* Printer 15 (Centronics 739)
*
ctl_15a dc.w    80,1,75,72
        dc.w    0
        dc.w    set_739-*,0,0,0
        dc.w    0,0,epi_cr-*-4,res_739-*-6
*
* Printer 16 (C.Itoh 7500)
* 
ctl_16a dc.w    80,1,60,72
        dc.w    0
        dc.w    set_ito-*,0,0,pre_itg-*-6
        dc.w    0,0,epi_nl-*-4,res_ito-*-6
ctl_16b dc.w    80,1,120,72
        dc.w    0
        dc.w    set_ite-*,0,0,pre_ito-*-6
        dc.w    0,0,epi_nl-*-4,res_itn-*-6
ctl_16c dc.w    80,1,160,72
        dc.w    0
        dc.w    set_itc-*,0,0,pre_ito-*-6
        dc.w    0,0,epi_nl-*-4,res_itn-*-6
*
* Printer 17 (Toshiba 7500)
*
ctl_17a dc.w    136,1,180,180
        dc.w    0
        dc.w    set_tos-*,0,0,pre_tos-*-6
        dc.w    0,0,epi_cn-*-4,res_tos-*-6
*
* Printer 18 (8056)
*
ctl_18a dc.w    80,1,70,72
        dc.w    0
        dc.w    set_856-*,0,0,pre_epk-*-6
        dc.w    0,0,epi_nl-*-4,res_ep6-*-6
*
* Printer 21 (ML82 OK Writer)
*
;**ctl_21a dc.w    80,1,100,67
;**        dc.w    0
;**        dc.w    set_m8c-*,0,0,0
;**        dc.w    0,0,epi_m82-*-4,res_m82-*-6
;**ctl_21b dc.w    80,1,60,67
;**        dc.w    0
;**        dc.w    set_m8n-*,0,0,0
;**        dc.w    0,0,epi_m82-*-4,res_m82-*-6
*
* Printer 23 (MT-80)
*
;**ctl_23a dc.w    75,1,170,82
;**        dc.w    0
;**        dc.w    set_mt8-*,0,0,pre_epl-*-6
;**        dc.w    0,0,epi_nl-*-4,res_ep6-*-6
;**ctl_23b dc.w    75,1,85,82
;**        dc.w    0
;**        dc.w    set_mt8-*,0,0,pre_epk-*-6
;**        dc.w    0,0,epi_nl-*-4,res_ep6-*-6
*
* Printer 24 (ML192)
*
;**ctl_24a dc.w    80,1,120,72
;**        dc.w    0
;**        dc.w    set_m9d-*,0,0,0
;**        dc.w    0,0,epi_m19-*-4,res_m19-*-6
;**ctl_24b dc.w    80,1,60,72
;**        dc.w    0
;**        dc.w    set_m9l-*,0,0,0
;**        dc.w    0,0,epi_m19-*-4,res_m19-*-6
;**ctl_24c dc.w    80,1,144,72
;**        dc.w    0
;**        dc.w    set_m9h-*,0,0,0
;**        dc.w    0,0,epi_m19-*-4,res_m19-*-6
*
* Printer 25 (ML193)
*
;**ctl_25a dc.w    136,1,120,72
;**        dc.w    0
;**        dc.w    set_m9d-*,0,0,0
;**        dc.w    0,0,epi_m19-*-4,res_m19-*-6
;**ctl_25b dc.w    136,1,60,72
;**        dc.w    0
;**        dc.w    set_m9l-*,0,0,0
;**        dc.w    0,0,epi_m19-*-4,res_m19-*-6
;**ctl_25c dc.w    136,1,144,72
;**        dc.w    0
;**        dc.w    set_m9h-*,0,0,0
;**        dc.w    0,0,epi_m19-*-4,res_m19-*-6
;**


        page
                      ; Epilogue definitions
epi_cn  dc.b    $d              crlf
epi_nl  dc.b    $a,0            epilogue is newline
epi_cr  dc.b    $d,0            end of colour pass is carriage return
res_gp1 dc.b    $f,0            SI reset GP100a to text mode
res_gp2 dc.b    esc,'L',3,0     reset GP250 to 1/6 inch spacing
res_ep6 dc.b    esc,'2',esc,'U',-2,0,0 reset Epson to 1/6 inch spacing bidirect
res_pjc dc.b    esc,'e',2,0     PJ1080A force last line of dump
epi_pad dc.b    -$12,10,0       pad with 4096 nulls
res_739 dc.b    esc,$13,0       Centronics 739 reset to normal
res_ty  dc.b    $1e,0           Tandy reset character mode
res_itn dc.b    esc,'N'         C.Itoh reset horizontal pitche
res_ito dc.b    esc,'A',0       C.Itoh reset vertical pitch
res_tos dc.b    esc,'U','0','6',0 Toshiba reset to 1/6"
;**epi_m19
;**epi_m82 dc.b    3,$e,0          graphics newline OK Writer / OKI 19x
;**res_m19
;**res_m82 dc.b    3,2,$1e,0       end graphics OK Writer / OKI 19x

*
* Preamble definitions
*
set_ep9 dc.b    esc,'U',1,esc,'A',8,0   set Epson type to 8/72" spacing
;**set_mt8 dc.b    esc,'U',1,esc,'A',7,0   set MT80 to 7/72"
set_856
set_hr4 dc.b    esc,'1',0               set Brother HR4 to 1/9" spacing
prc_ep  dc.b    esc,'r',-$10,0          Epson select colour
pre_epk dc.b    esc,'K',-6,2,1,0        set Epson type single density graphics
pre_epl dc.b    esc,'L',-6,2,1,0        set Epson type double density graphics
;**pre_eps dc.b    esc,'*',-2,0,-6,2,1,0   set Epson type single density graphics
pre_epb dc.b    esc,'*',4,-6,2,1,0      set Epson type CRT graphics
;**pre_epp dc.b    esc,'*',5,-6,2,1,0      set Epson type plotter graphics
pre_epc dc.b    esc,'*',6,-6,2,1,0      set Epson type CRT II graphics
pre_epd dc.b    esc,'*',33,-6,2,1,0     set Epson 24 DD
pre_ept dc.b    esc,'*',39,-6,2,1,0     set Epson 24 TD
set_gp2 dc.b    esc,'L',2,0             set gp250x to 1/9" spacing
set_gp7 dc.b    esc,'T','1','2',0       set gp700a to 1/10" spacing
set_gp1 dc.b    8,0                     gp100a graphics
pre_gp2 dc.b    esc,'G',-4,2,1,0        gp250x graphics
prc_gp7 dc.b    $14,-$10                gp700a set colour
        dc.b    esc,$10,-2,0,-2,0              set position
        dc.b    esc,'K',-8,3,1,0               set number of dots
pre_pjc dc.b    esc,'X',-4,1,8,0        Canon PJ1080A graphics
set_ol3 dc.b    esc,'G','1',';',-8,3,8,';',-$e,2,8,';','3',esc,'Z',0
set_ol2 dc.b    esc,'G','1',';',-8,3,8,';',-$e,2,8,esc,'Z',0
set_tyn dc.b    esc,$13,$12,0           Tandy 60 dpi
set_tyc dc.b    esc,$14,$12,0           Tandy 100 dpi
pre_ty1 dc.b    esc,$10,-2,0,-2,0,0     Tandy column 1
set_739 dc.b    esc,'%','0',0           Set Centronics 739 to graphics
set_itc dc.b    esc,'Q'                 C.Itoh compressed
set_ito dc.b    esc,'T','1','6',0       C.Itoh 16/144"
set_ite dc.b    esc,'E',esc,'T','1','6',0 C.Itoh elite 16/144"
pre_ito dc.b    esc,'S',-8,4,1,0        C.Itoh Graphics
pre_itg dc.b    esc,'G',-8,4,1,0        C.Itoh expanded graphics
set_tos dc.b    esc,'U','0','7',0       Toshiba set 2/15"
pre_tos dc.b    esc,'I',-4,2,1,0        Toshiba graphics
;**set_m8n dc.b    $1e,3,0                 OK graphics, normal density
;**set_m8c dc.b    $1d,3,0                 OK graphics, compressed
;**set_m9l dc.b    esc,$50,$1e,3,0         ML 19x 60 dpi
;**set_m9d dc.b    esc,$52,$1e,3,0         ML 19x 120 dpi
;**set_m9h dc.b    esc,$52,$1c,3,0         ML 19x 144 dpi
        end
