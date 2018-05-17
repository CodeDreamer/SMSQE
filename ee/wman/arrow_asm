* Blobs for arrows   V0.00    1986  Tony Tebby  QJUMP
*
        section wman
*
        xdef    wm_uparr
        xdef    wm_dnarr
        xdef    wm_ltarr
        xdef    wm_rtarr
*
wm_uparr
        dc.w    $0100,0         mode 4, no adaption
        dc.w    10,4            10 by 4
        dc.w    4,0             origin centre top
        dc.l    0
        dc.l    wm_uparm-*
        dc.l    *+4-*

        dc.w    $0101,0         mode 8
        dc.w    10,4
        dc.w    4,0
        dc.l    0
        dc.l    wm_uparm-*
        dc.l    0

wm_uparm
        dc.w    %0000110000001100,%0000000000000000
        dc.w    %0011111100111111,%0000000000000000
        dc.w    %1111111111111111,%1100000011000000
        dc.w    %0000110000001100,%0000000000000000
*
wm_dnarr
        dc.w    $0100,0         mode 4, no adaption
        dc.w    10,4            10 by 4
        dc.w    4,3             origin centre bottom
        dc.l    0
        dc.l    wm_dnarm-*
        dc.l    *+4-*

        dc.w    $0101,0         mode 8
        dc.w    10,4
        dc.w    4,3
        dc.l    0
        dc.l    wm_dnarm-*
        dc.l    0

wm_dnarm
        dc.w    %0000110000001100,%0000000000000000
        dc.w    %1111111111111111,%1100000011000000
        dc.w    %0011111100111111,%0000000000000000
        dc.w    %0000110000001100,%0000000000000000
*
wm_ltarr
        dc.w    $0100,0         mode 4, no adaption
        dc.w    8,5             8 by 5
        dc.w    0,2             origin centre left
        dc.l    0
        dc.l    wm_ltarm-*
        dc.l    *+4-*

        dc.w    $0101,0         mode 8
        dc.w    8,5
        dc.w    0,2
        dc.l    0
        dc.l    wm_ltarm-*
        dc.l    0

wm_ltarm
        dc.w    %0000110000001100,%0000000000000000
        dc.w    %0011110000111100,%0000000000000000
        dc.w    %1111111111111111,%0000000000000000
        dc.w    %0011110000111100,%0000000000000000
        dc.w    %0000110000001100,%0000000000000000
*
wm_rtarr
        dc.w    $0100,0         mode 4, no adaption
        dc.w    8,5             8 by 5
        dc.w    7,2             origin centre right
        dc.l    0
        dc.l    wm_rtarm-*
        dc.l    *+4-*

        dc.w    $0101,0         mode 8
        dc.w    8,5
        dc.w    6,2
        dc.l    0
        dc.l    wm_rtarm-*
        dc.l    0

wm_rtarm
        dc.w    %0011000000110000,%0000000000000000
        dc.w    %0011110000111100,%0000000000000000
        dc.w    %1111111111111111,%0000000000000000
        dc.w    %0011110000111100,%0000000000000000
        dc.w    %0011000000110000,%0000000000000000
*
        end
