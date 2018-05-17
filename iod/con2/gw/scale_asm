	section gw
* sets graphics scale factor
	xdef	GW_SCALE

	include 'dev8_Minerva_INC_SD'
	include 'dev8_Minerva_INC_assert'

gw_scale
	assert	SD_YORG,SD_XORG-6,SD_SCAL-12
	lea	SD_YORG(A0),A2
	moveq	#3*6,D0
loop
	move.b	(A1)+,(A2)+
	subq.b	#1,D0
	bne.s	LOOP

	rts

	end
