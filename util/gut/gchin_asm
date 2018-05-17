*       Given a channel address, make a name and ID in the row entry
*       table, and point the current object to the name.
*
*       Registers:
*               Entry                           Exit
*       D1      channel number                  preserved
*       D2      pointer to cdb                  preserved
*       D6                                      required status
*       A0      row entry to modify             preserved
*       A2      object pointer in object list   updated (+4)
*
        section setup
*
        include 'dev8_qram_keys'
        include 'dev8_ptr_keys'
        include 'dev8_wman_wstatus'
*
        xref    qr_cnvnm
*
        xdef    cl_gchin
        xdef    cl_cpstr
*
mkchreg reg     d1/d2/a0-a4
cstk_d1 equ     $00
cstk_a0 equ     $08
cstk_a2 equ     $10
*
cl_gchin
        movem.l mkchreg,-(sp)           ; save smashed registers
        moveq   #wsi.avbl,d6            ; assume this one will be available
        move.l  d2,a4                   ; a more useful pointer register!
*
*       Fill in the channel ID
*
        move.l  ch_tag(a4),d0           ; get the tag...
        move.w  d1,d0                   ; ...and the channel number
        move.l  d0,cl_devid(a0)         ; fill in channel ID
*
        moveq   #mt.inf,d0
        trap    #1
        move.l  a0,a3                   ; save pointer to system variables
        lea.l   sv_ddlst(a0),a0         ; point to directory driver list
        move.l  ch_drivr(a4),d0         ; and this channel's driver
*
mkc_ddch
        move.l  (a0),d1                 ; next directory driver
        beq.s   mkc_serd                ; isn't one, it's a serial I/O channel
        move.l  d1,a0                   ; point to next
        cmp.l   d0,a0                   ; is it the same driver?
        bne.s   mkc_ddch                ; no, check next
*
*       Come here if the channel is open to a directory device.
*
        move.l  cstk_a0(sp),a1          ; point to row entry
        lea     cl_devnm+2(a1),a1       ; and where the device name should go
*
        lea     ch_drnam(a0),a2         ; point to drive name
        bsr     cpstr                   ; copy the string
*
        moveq   #0,d0                   ; get...
        move.b  fs_drive(a4),d0         ; ...this channel's drive ID 
        lsl.w   #2,d0                   ; physical drive number
        lea     sv_fsdef(a3),a2         ; point to physical def ptr table
        move.l  0(a2,d0.w),a2           ; and thus to physical defn block
        move.b  fs_drivn(a2),d0         ; and (phew!) drive number
        subq.b  #1,d0
        and.b   #7,d0                   ; fix 0-7 in case...
        add.b   #'1',d0                 ; make it ASCII
        move.b  d0,(a1)+                ; fill it in
        move.b  #'_',(a1)+              ; and underscore at end
*
mkc_cdev
        lea     fs_fname(a4),a2         ; copy the filename itself
        bra.s   mkc_sern                ; fill in as if it were a serial name
*
*       Come here to fill in a serial I/O name.  Most serial drivers have a
*       string of less that 8 characters within 16 bytes of their start,
*       for use with the IO.NAME utility.  We search for that, unless there
*       is no open routine, in which case the channel is a CON or SCR 
*       of the pointer interface.
*
mkc_serd
        move.l  ch_drivr(a4),a2         ; point to driver...
        move.l  ch_open(a2),d0          ; ...and the open routine
        beq.s   mkc_scon                ; there isn't one, it's a CON
        move.l  d0,a2
*
        moveq   #15,d0                  ; search 16 words
mkc_smax
        moveq   #8,d1                   ; for one less than $0008
mkc_sopl
        cmp.w   (a2)+,d1
        dbhi    d0,mkc_sopl             ; search until found
        bls.s   mkc_nf                  ; no success
        move.l  a2,a1                   ; point to characters
        move.w  -(a2),d1                ; success, get count of characters
        bra.s   mkc_ccae                ; and check them
mkc_cclp
        cmp.b   #'A',(a1)               ; must be in 'A'...
        bcs.s   mkc_ccfl                ; (...oops)
        cmp.b   #'Z',(a1)+              ; ... to 'Z'
        bhi.s   mkc_ccfl                ; (...oops)
mkc_ccae
        dbra    d1,mkc_cclp
        bra.s   mkc_cbuf                ; success, we found it
*
mkc_ccfl
        addq.l  #2,a2                   ; point to next word
        bra.s   mkc_smax                ; and look again
*
mkc_nf
        lea     serdev(pc),a2           ; point to default string
        bra.s   mkc_cbuf                ; and copy to buffer
*
*       It's a pointer CON, so set it up
*
mkc_scon
        lea     condev(pc),a2           ; point to appropriate string
*
mkc_cbuf
        move.l  cstk_a0(sp),a1
        add.w   #cl_devnm+2,a1          ; point to where name goes
*
mkc_sern
        move.l  2(a2),d0                ; get first four characters
        cmp.l   #$53455200,d0           ; SERial port?
        bne.s   mkc_cprt                ; no, see if it's PRT
        cmp.l   #$C000,a2               ; is the open routine in ROM?
        bcs     mkc_serp                ; yes, hack some parameters onto the end
*
mkc_cprt
        cmp.l   #'PRT%',d0              ; is it a PRT?
        bne.s   mkc_ccon                ; no, might be a CON
        bsr     cpstr                   ; copy the PRT
        move.b  #' ',(a1)+              ; and a space
        bra     mkc_cdev                ; and copy the device name
*
mkc_ccon
        cmp.l   #'CON_',d0              ; is it a CONsole device?
        beq.s   mkc_csiz                ; yes, add size
        cmp.w   #3,(a2)                 ; is it three long...
        bne     mkc_cpy                 ; no, just copy name
        cmp.l   #$434f4e00,d0           ; ...and 'CON'?
        bne     mkc_cpy
        moveq   #wsi.unav,d6            ; yes, those aren't available
        bra     mkc_cpy
*
*       Add size onto CON_ channels
*
mkc_csiz
        bsr     cpstr                   ; copy it
        move.l  a1,a0                   ; where number conversions go to
*
        move.w  sd_borwd+sd.extnl(a4),d1 ; get border
        add.w   d1,d1
        add.w   d1,d1
*        
        move.w  sd_xsize+sd.extnl(a4),-(sp) ; add size...
        move.l  a7,a1
        add.w   d1,(a1)
        jsr     qr_cnvnm(pc)
        move.b  #'x',(a0)+
*
        move.l  a7,a1
        move.w  sd_ysize+sd.extnl(a4),(a1)
        asr.w   #1,d1
        add.w   d1,(a1)
        jsr     qr_cnvnm(pc)
        move.b  #'a',(a0)+
*
        move.l  a7,a1
        move.l  sd_xmin+sd.extnl(a4),(a1)   ; ...and origin information
        sub.w   d1,(a1)
        jsr     qr_cnvnm(pc)
        move.b  #'x',(a0)+
*
        move.l  a7,a1
        move.l  sd_ymin+sd.extnl(a4),(a1)
        asr.w   #1,d1
        sub.w   d1,(a1)
        jsr     qr_cnvnm(pc)
        addq.l  #2,sp                   ; pop buffer
*
        move.l  a0,a1                   ; where name has got to
        bra.s   mkc_mkln                ; set the length
*
*       Hack the parameters onto the end of a SER name: the format expected
*       in the cdb is:
*
*       $18.w   port number 1..2
*       $1A.w   parity      0=none, 1=odd, 2=even, 3=mark, 4=space
*       $1C.w   handshake   -1=default, 0=ignore, 1=handshake (<>0 => handshake)
*       $1E.w   EOL/EOF     -2=default, -1=raw data, 0=EOL=CR/EOF=^Z, 1=EOL=CR
*
serpar  dc.b    ' oems'
serhnd  dc.b    'ih'
sereof  dc.b    'rzc'
        ds.w    0
*
mkc_serp
        bsr.s   cpstr                   ; put in the 'SER'
        move.w  se_portn(a4),d0
        add.b   #'0',d0
        move.b  d0,(a1)+                ; add port number
*
        move.w  se_parit(a4),d0
        beq.s   mkc_srnp                ; no parity specified
        move.b  serpar(pc,d0.w),(a1)+
*
mkc_srnp
        move.w  se_hands(a4),d0
        bmi.s   mkc_srnh                ; no handshake specified
        move.b  serhnd(pc,d0.w),(a1)+
*
mkc_srnh
        move.w  se_eofch(a4),d0
        addq.w  #1,d0                   ; EOF starts at -2 for default
        bmi.s   mkc_mkln                ; no EOF specified
        move.b  sereof(pc,d0.w),(a1)+
        bra.s   mkc_mkln                ; now set length
*
mkc_cpy
        bsr.s   cpstr
mkc_mkln
        move.l  cstk_a0(sp),a2          ; start of name again
        add.w   #cl_devnm+2,a2
        sub.l   a2,a1                   ; length of it
        move.w  a1,-(a2)                ; fill it in
        move.l  a2,d0
*
        movem.l (sp)+,mkchreg           ; restore registers
        move.l  d0,(a2)+                ; fill in object pointer
        rts
*
*       Copy the characters of a string
*
*       Registers:
*               Entry                           Exit
*       D0                                      smashed
*       A1      destination buffer              updated
*       A2      source string                   smashed
*
cl_cpstr
cpstr
        move.w  (a2)+,d0
        bra.s   cps_lpe
cps_lp
        move.b  (a2)+,(a1)+
cps_lpe
        dbra    d0,cps_lp
        rts
*
serdev  dc.w    srend-*-2
        dc.b    '<Serial I/O>'
srend
*
condev  dc.w    cnend-*-2
        dc.b    'CON_'
cnend
        ds.w    0
*
        end

